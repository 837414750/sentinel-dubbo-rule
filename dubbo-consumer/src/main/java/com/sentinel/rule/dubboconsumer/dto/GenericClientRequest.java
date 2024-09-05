package com.sentinel.rule.dubboconsumer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericClientRequest {
    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 服务接口全限定名
     */
    private String serviceName;

    /**
     * 要调用的方法名
     */
    private String methodName;

    /**
     * 方法参数类型的数组
     */
    private String[] parameterTypes;

    /**
     * 方法参数值的数组
     */
    private Object[] args;

    /**
     * 服务的分组名
     */
    private String group;
}