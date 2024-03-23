package com.demo.using.thirdparty.lib.thirdpartylibdemo;

import com.main.App;
import com.notmain.AnotherApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public App app(){
        return new App();
    }

    @Bean
    public AnotherApp anotherApp(){
        return new AnotherApp();
    }
}
