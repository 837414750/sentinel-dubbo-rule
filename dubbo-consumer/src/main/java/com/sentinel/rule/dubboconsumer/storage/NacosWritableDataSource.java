package com.sentinel.rule.dubboconsumer.storage;

import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 将sentinel规则写入到nacos配置中心
 * @param <T>
 */
@Slf4j
public class NacosWritableDataSource<T> implements WritableDataSource<T> {
 
    private final Converter<T, String> configEncoder;
    private final NacosDataSourceProperties nacosDataSourceProperties;
 
    private final Lock lock = new ReentrantLock(true);
    private ConfigService configService = null;
 
    public NacosWritableDataSource(NacosDataSourceProperties nacosDataSourceProperties, Converter<T, String> configEncoder) {
        if (configEncoder == null) {
            throw new IllegalArgumentException("Config encoder cannot be null");
        }
        if (nacosDataSourceProperties == null) {
            throw new IllegalArgumentException("Config nacosDataSourceProperties cannot be null");
        }
        this.configEncoder = configEncoder;
        this.nacosDataSourceProperties = nacosDataSourceProperties;
        final Properties properties = buildProperties(nacosDataSourceProperties);
        try {
            // 也可以直接注入NacosDataSource，然后反射获取其configService属性
            this.configService = NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            log.error("create configService failed.", e);
        }
    }
 
    private Properties buildProperties(NacosDataSourceProperties nacosDataSourceProperties) {
        Properties properties = new Properties();
        if (!StringUtils.isEmpty(nacosDataSourceProperties.getServerAddr())) {
            properties.setProperty(PropertyKeyConst.SERVER_ADDR, nacosDataSourceProperties.getServerAddr());
        } else {
            properties.setProperty(PropertyKeyConst.ACCESS_KEY, nacosDataSourceProperties.getAccessKey());
            properties.setProperty(PropertyKeyConst.SECRET_KEY, nacosDataSourceProperties.getSecretKey());
            properties.setProperty(PropertyKeyConst.ENDPOINT, nacosDataSourceProperties.getEndpoint());
        }
        if (!StringUtils.isEmpty(nacosDataSourceProperties.getNamespace())) {
            properties.setProperty(PropertyKeyConst.NAMESPACE, nacosDataSourceProperties.getNamespace());
        }
        if (!StringUtils.isEmpty(nacosDataSourceProperties.getUsername())) {
            properties.setProperty(PropertyKeyConst.USERNAME, nacosDataSourceProperties.getUsername());
        }
        if (!StringUtils.isEmpty(nacosDataSourceProperties.getPassword())) {
            properties.setProperty(PropertyKeyConst.PASSWORD, nacosDataSourceProperties.getPassword());
        }
        return properties;
    }
 
    @Override
    public void write(T value) throws Exception {
        lock.lock();
        // todo handle cluster concurrent problem
        try {
            String convertResult = configEncoder.convert(value);
            if (configService == null) {
                log.error("configServer is null, can not continue.");
                return;
            }
            // 规则配置数据推送到nacos配置中心
            final boolean published = configService.publishConfig(nacosDataSourceProperties.getDataId(), nacosDataSourceProperties.getGroupId(), convertResult);
            if (!published) {
                log.error("sentinel {} publish to nacos failed.", nacosDataSourceProperties.getRuleType());
            }
        } finally {
            lock.unlock();
        }
    }
 
    @Override
    public void close() throws Exception {
 
    }
}
