package com.sentinel.rule.dubboconsumer.controller;

import com.sentinel.rule.dubboconsumer.remote.GenericInvokeClient;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/generic")
public class GenericDubboController {

    @Resource
    private GenericInvokeClient genericInvokeClient;

    @RequestMapping(value = "/test1")
    public Object test1(@RequestBody UserRequest userRequest) {
        return genericInvokeClient.getResult(userRequest);
    }
}
