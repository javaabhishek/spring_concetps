package com.study.axon.cqrs.productservice.query;

import com.study.axon.cqrs.productservice.core.data.ProductEntity;
import com.study.axon.cqrs.productservice.core.data.ProductsRepository;
import com.study.axon.cqrs.productservice.core.events.ProductCreatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private final ProductsRepository productsRepository;

    public ProductEventHandler(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @ExceptionHandler(resultType=Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;//re-throwing error from event handler to ProductsServiceEventsErrorHandler
    }

    @ExceptionHandler(resultType=IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
        // Log error message
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) throws Exception{
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        try {
            productsRepository.save(productEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw ex;
        }

        throw new Exception("Sample exception from  Product event handler");
    }
}
