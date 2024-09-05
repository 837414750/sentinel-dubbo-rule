package com.sentinel.rule.dubboconsumer.remote;

import com.sentinel.rule.dubboconsumer.dto.GenericClientRequest;
import com.sentinel.rule.dubboconsumer.service.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.springframework.stereotype.Component;

@Component
public class GenericDubboClientService {

    public Object invoke(GenericClientRequest request) {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(request.getApplicationName());

        // 创建 ReferenceConfig，用于构造 GenericService
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setInterface(request.getServiceName());
        reference.setGeneric(true);
        if (StringUtils.isNotBlank(request.getGroup())) {
            reference.setGroup(request.getGroup());
        }

        try {
            // 获取 GenericService 实例
            GenericService genericService = reference.get();
            // 执行泛化调用
            return genericService.$invoke(request.getMethodName(), request.getParameterTypes(), request.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("dubbo远程调用异常");
        }
    }

    /**
     * 通过服务名称、方法名、参数类型和参数值动态调用 Dubbo 服务
     *
     * @param request
     * @return
     */
    public Object invoke(GenericClientRequest request, RateLimiter rateLimiter) {

        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(request.getApplicationName());

        // 创建 ReferenceConfig，用于构造 GenericService
        ReferenceConfig<GenericService> reference = new ReferenceConfig<>();
        reference.setApplication(applicationConfig);
        reference.setInterface(request.getServiceName());
        reference.setGeneric(true);
        if (StringUtils.isNotBlank(request.getGroup())) {
            reference.setGroup(request.getGroup());
        }

        try {
            // 获取 GenericService 实例
            GenericService genericService = reference.get();
            // 执行泛化调用
            return rateLimiter.execute((Object... p) -> genericService.$invoke(request.getMethodName(), request.getParameterTypes(), request.getArgs()), request.getArgs());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("dubbo远程调用异常");
        }
    }
}