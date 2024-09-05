package com.sentinel.rule.dubboconsumer.storage;

import com.alibaba.cloud.sentinel.SentinelProperties;
import com.alibaba.cloud.sentinel.datasource.RuleType;
import com.alibaba.cloud.sentinel.datasource.config.DataSourcePropertiesConfiguration;
import com.alibaba.cloud.sentinel.datasource.config.NacosDataSourceProperties;
import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.SmartInitializingSingleton;

import java.util.List;

/**
 * sentinel 规则持久化到 nacos配置中心
 */
public class SentinelNacosDataSourceHandler implements SmartInitializingSingleton {

    private final SentinelProperties sentinelProperties;

    public SentinelNacosDataSourceHandler(SentinelProperties sentinelProperties) {
        this.sentinelProperties = sentinelProperties;
    }

    @Override
    public void afterSingletonsInstantiated() {
        sentinelProperties.getDatasource().values().forEach(this::registryWriter);
    }

    private void registryWriter(DataSourcePropertiesConfiguration dataSourceProperties) {
        final NacosDataSourceProperties nacosDataSourceProperties = dataSourceProperties.getNacos();
        if (nacosDataSourceProperties == null) {
            return;
        }
        final RuleType ruleType = nacosDataSourceProperties.getRuleType();
        switch (ruleType) {
            case FLOW:
                WritableDataSource<List<FlowRule>> flowRuleWriter = new NacosWritableDataSource<>(nacosDataSourceProperties, JSON::toJSONString);
                WritableDataSourceRegistry.registerFlowDataSource(flowRuleWriter);
                break;
            case DEGRADE:
                WritableDataSource<List<DegradeRule>> degradeRuleWriter = new NacosWritableDataSource<>(nacosDataSourceProperties, JSON::toJSONString);
                WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWriter);
                break;
            case PARAM_FLOW:
                WritableDataSource<List<ParamFlowRule>> paramFlowRuleWriter = new NacosWritableDataSource<>(nacosDataSourceProperties, JSON::toJSONString);
                ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWriter);
                break;
            case SYSTEM:
                WritableDataSource<List<SystemRule>> systemRuleWriter = new NacosWritableDataSource<>(nacosDataSourceProperties, JSON::toJSONString);
                WritableDataSourceRegistry.registerSystemDataSource(systemRuleWriter);
                break;
            case AUTHORITY:
                WritableDataSource<List<AuthorityRule>> authRuleWriter = new NacosWritableDataSource<>(nacosDataSourceProperties, JSON::toJSONString);
                WritableDataSourceRegistry.registerAuthorityDataSource(authRuleWriter);
                break;
            default:
                break;
        }
    }

    public List<ParamFlowRule> getRules() {
        return ParamFlowRuleManager.getRules();
    }
}
