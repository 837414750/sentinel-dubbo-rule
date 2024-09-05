package com.sentinel.rule.dubboconsumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RateLimiterStrategy {

    private final Map<String, RateLimiter> rateLimiters;

    @Autowired
    public RateLimiterStrategy(Map<String, RateLimiter> rateLimiters) {
        this.rateLimiters = rateLimiters;
    }

    public RateLimiter getRateLimiter(String strategyName) {
        return rateLimiters.get(strategyName);
    }
}