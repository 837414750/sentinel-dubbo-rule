package com.sentinel.rule.dubboconsumer.service;

@FunctionalInterface
public interface CheckedFunction<T> {
    T apply(Object... params) throws Exception;

    default Object[] getParams(Object... params) {
        return params;
    }
}
