package com.study.axon.saga.orchestration.orderservice;

import com.study.axon.saga.orchestration.orderservice.config.AxonConfig;
import org.axonframework.config.Configuration;
import org.axonframework.config.ConfigurationScopeAwareProvider;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.SimpleDeadlineManager;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonConfig.class })
public class OrderserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderserviceApplication.class, args);
	}

	@Bean
	public DeadlineManager deadlineManager(Configuration configuration
	, SpringTransactionManager transactionManager){
		return
				SimpleDeadlineManager.builder()
						.scopeAwareProvider(new ConfigurationScopeAwareProvider(configuration))
						.transactionManager(transactionManager)
						.build();
	}

}
