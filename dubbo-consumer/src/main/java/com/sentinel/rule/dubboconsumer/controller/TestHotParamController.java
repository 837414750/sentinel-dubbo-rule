package com.sentinel.rule.dubboconsumer.controller;

import com.sentinel.rule.dubboconsumer.service.TestHotParamService;
import com.sentinel.rule.dubbointeface.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestHotParamController {

    @Autowired
    private TestHotParamService testHotParamService;

    @RequestMapping("/queryHot")
    public String queryHot(@RequestBody UserRequest userRequest) {
        return testHotParamService.queryHot(userRequest);
    }
}