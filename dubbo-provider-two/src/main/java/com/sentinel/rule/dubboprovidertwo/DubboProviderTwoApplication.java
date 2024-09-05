package com.sentinel.rule.dubboprovidertwo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class DubboProviderTwoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboProviderTwoApplication.class, args);
    }

}
