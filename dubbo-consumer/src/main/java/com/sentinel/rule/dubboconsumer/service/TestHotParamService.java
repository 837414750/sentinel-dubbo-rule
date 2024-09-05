package com.sentinel.rule.dubboconsumer.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestHotParamService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @SentinelResource("queryHot-sentinel")
    public String queryHot(UserRequest userRequest) {

        return (String) execute(userRequest);
    }

    public Object execute(UserRequest userRequest) {
        Entry entry = null;
        try {
            entry = SphU.entry("queryHot-sentinel", EntryType.IN, 1, userRequest.getName());

            // Your logic here.
            return "Hello " + userRequest.getName();
        } catch (BlockException ex) {
            // Handle request rejection.
            log.error("异常：", ex);
            return "~~~~~~限流了·~~~~~~";
        } finally {
            if (entry != null) {
                entry.exit(1, userRequest.getCode());
            }
        }
    }

}