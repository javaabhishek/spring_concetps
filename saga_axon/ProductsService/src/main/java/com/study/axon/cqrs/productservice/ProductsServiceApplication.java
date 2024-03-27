package com.study.axon.cqrs.productservice;

import com.study.axon.cqrs.productservice.command.interceptors.CreateProductCommandInterceptor;
import com.study.axon.cqrs.productservice.config.AxonConfig;
import com.study.axon.cqrs.productservice.core.errorhandling.ProductsServiceEventsErrorHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.config.EventProcessingConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableDiscoveryClient
@Import({ AxonConfig.class })
public class ProductsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsServiceApplication.class, args);
	}

	@Autowired
	public void registerInterceptors(ApplicationContext applicationContext, CommandBus commandBus){
		commandBus.registerDispatchInterceptor(applicationContext.getBean(CreateProductCommandInterceptor.class));
	}

	@Autowired
	public void configure(EventProcessingConfigurer config){
		config.registerListenerInvocationErrorHandler("product-group",
				conf -> new ProductsServiceEventsErrorHandler());
	}
}
