package com.sentinel.rule.dubboconsumer.config;

import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowItem;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class SentinelConfigManager {

    @PostConstruct
    public void initHotParamFlowRules() {
        ParamFlowRule rule = new ParamFlowRule("dubboServiceWithHotParam").setParamIdx(0)  // 对第1个参数进行限流
                .setCount(10);    // 基础限流阈值
        //参数例外项
        ParamFlowItem item = new ParamFlowItem().setObject("one").setCount(2);//针对特定的code进行限流 code=two 进行限流，设置为QPS=2
        ParamFlowItem item2 = new ParamFlowItem().setObject("two").setCount(3);

        rule.setParamFlowItemList(List.of(item, item2));

        ParamFlowRuleManager.loadRules(Collections.singletonList(rule));
    }
}