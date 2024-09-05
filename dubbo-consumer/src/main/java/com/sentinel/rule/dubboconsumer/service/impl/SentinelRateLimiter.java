package com.sentinel.rule.dubboconsumer.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sentinel.rule.dubboconsumer.service.CheckedFunction;
import com.sentinel.rule.dubboconsumer.service.RateLimiter;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class SentinelRateLimiter implements RateLimiter {

    @Override
    public Object execute(CheckedFunction<Object> function, Object... param) throws Exception {
        //获取请求对象
        UserRequest userRequest = (UserRequest) param[0];
        try (Entry entry = SphU.entry("dubboServiceWithHotParam", EntryType.IN, 1, userRequest.getCode())) { // 使用UserReq的code进行限流
            return function.apply();
        } catch (BlockException ex) {
            System.out.println("Request is rate-limited due to hot parameters");
            throw ex;
        }
    }
}