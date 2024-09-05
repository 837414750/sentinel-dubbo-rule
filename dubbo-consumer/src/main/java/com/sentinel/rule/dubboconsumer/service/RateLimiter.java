package com.sentinel.rule.dubboconsumer.service;

@FunctionalInterface
public interface RateLimiter {
    Object execute(CheckedFunction<Object> function, Object... param) throws Exception;
}