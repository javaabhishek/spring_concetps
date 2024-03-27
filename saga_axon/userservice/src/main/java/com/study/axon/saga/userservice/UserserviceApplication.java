package com.study.axon.saga.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import com.study.axon.saga.userservice.config.AxonConfig;
@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonConfig.class })
public class UserserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserserviceApplication.class, args);
	}

}
