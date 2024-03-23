package com.example.ratelimitor.ratelimitor_app.controller;

import com.example.ratelimitor.ratelimitor_app.services.HelloService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private HelloService helloService;
    @GetMapping("/sayHello/{username}")
    @RateLimiter(name="backendA")
    public String sayHello(@PathVariable String username){
        return helloService.sayhello(username);
    }

}
