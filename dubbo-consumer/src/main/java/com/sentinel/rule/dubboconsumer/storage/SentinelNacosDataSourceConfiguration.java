package com.sentinel.rule.dubboconsumer.storage;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.custom.SentinelAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(SentinelAutoConfiguration.class)
public class SentinelNacosDataSourceConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SentinelNacosDataSourceHandler sentinelNacosDataSourceHandler(SentinelProperties sentinelProperties) {
        return new SentinelNacosDataSourceHandler(sentinelProperties);
    }
}