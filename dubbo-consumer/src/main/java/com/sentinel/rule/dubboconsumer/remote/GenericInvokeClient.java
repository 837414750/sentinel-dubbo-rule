package com.sentinel.rule.dubboconsumer.remote;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.sentinel.rule.dubboconsumer.dto.GenericClientRequest;
import com.sentinel.rule.dubboconsumer.service.RateLimiter;
import com.sentinel.rule.dubboconsumer.service.RateLimiterStrategy;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
public class GenericInvokeClient {

    @Resource
    private GenericDubboClientService generic;

    private RateLimiterStrategy rateLimiterStrategy;

    @Autowired
    public GenericInvokeClient(RateLimiterStrategy rateLimiterStrategy) {
        this.rateLimiterStrategy = rateLimiterStrategy;
    }

    @SentinelResource(value = "dubboServiceWithHotParam")
    public Object getResult(UserRequest userRequest) {
        //进行泛化调用，并使用参数流控
        GenericClientRequest request = new GenericClientRequest();
        request.setApplicationName("consumer");
        request.setServiceName("com.sentinel.rule.dubbointeface.DemoService");
        request.setMethodName("sayHello");

        request.setParameterTypes(new String[]{"com.sentinel.rule.dubbointeface.dto.UserRequest"});

        //准备参数
        userRequest.setPrice(BigDecimal.valueOf(100.05));
        userRequest.setFlag(true);
        request.setArgs(new Object[]{userRequest});

        request.setGroup(userRequest.getGroup());

        RateLimiter rateLimiter = rateLimiterStrategy.getRateLimiter("sentinelRateLimiter");

        return generic.invoke(request, rateLimiter);
    }
}