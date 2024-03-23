package com.example.ratelimitor.ratelimitor_app.config;


import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

//@Configuration
public class RateLimiterCfg {

   /* @Bean
    public RateLimiter rateLimiter(){

        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitForPeriod(5)//The number of allowed requests during one “limit-refresh-period”.
                .limitRefreshPeriod(Duration.ofSeconds(15))//Specifies the duration after which “limit-for-period” will be reset.
                .timeoutDuration(Duration.ofSeconds(15))//Sets the maximum wait time for the rate limiter to permit subsequent requests.
                .build();
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);
        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("myLimiter");
        return rateLimiterWithDefaultConfig;
    }*/
}
