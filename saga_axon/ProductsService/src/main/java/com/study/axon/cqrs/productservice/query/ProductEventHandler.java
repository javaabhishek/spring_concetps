package com.study.axon.cqrs.productservice.query;

import com.study.axon.cqrs.productservice.core.data.ProductEntity;
import com.study.axon.cqrs.productservice.core.data.ProductsRepository;
import com.study.axon.cqrs.productservice.core.events.ProductCreatedEvent;
import com.study.axon.saga.core.events.ProductReservationCancelledEvent;
import com.study.axon.saga.core.events.ProductReservedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("product-group")
public class ProductEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductEventHandler.class);

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
    public void on(ProductCreatedEvent productCreatedEvent) //throws Exception
    {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productCreatedEvent, productEntity);

        try {
            productsRepository.save(productEntity);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            throw ex;
        }

        //throw new Exception("Sample exception from  Product event handler");
    }

    @EventHandler
    public void on(ProductReservedEvent productReservedEvent){
        ProductEntity productEntity= productsRepository.findByProductId(productReservedEvent.getProductId());
        int updatedQty=productEntity.getQuantity()- productReservedEvent.getQuantity();
        productEntity.setQuantity(updatedQty);
        productsRepository.save(productEntity);

        LOGGER.info("ProductEventHandler is called for  ProductReservedEvent for orderId: " + productReservedEvent.getOrderId() +
                " and productId: " + productReservedEvent.getProductId() );
    }

    @EventHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        ProductEntity productEntity= productsRepository.findByProductId(productReservationCancelledEvent.getProductId());
        int updatedQty=productEntity.getQuantity()+ productReservationCancelledEvent.getQuantity();
        productEntity.setQuantity(updatedQty);
        productsRepository.save(productEntity);

        LOGGER.info("ProductEventHandler is called for  ProductReservationCancelledEvent for orderId: "
                + productReservationCancelledEvent.getOrderId() +
                " and productId: " + productReservationCancelledEvent.getProductId() );
    }

}
