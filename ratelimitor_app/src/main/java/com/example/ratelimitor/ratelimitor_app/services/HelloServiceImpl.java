package com.example.ratelimitor.ratelimitor_app.services;

import org.springframework.stereotype.Service;

@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public String sayhello(String username) {
        return String.format("Hello %s , Welcome to learning world",username);
    }
}
