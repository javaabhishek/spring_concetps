package com.study.axon.cqrs.productservice.command;

import com.study.axon.cqrs.productservice.core.events.ProductCreatedEvent;
import com.study.axon.saga.core.commands.CancelProductReservationCommand;
import com.study.axon.saga.core.commands.ReserveProductCommand;
import com.study.axon.saga.core.events.ProductReservationCancelledEvent;
import com.study.axon.saga.core.events.ProductReservedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    ProductAggregate(){}

    @CommandHandler // command handler : Handle/capture the command that is raised by ProductCommandController.createProduct endpoint
    ProductAggregate(CreateProductCommand createProductCommand) //throws Exception
    {

        //business logic (i.e validation and etc...)
        if(createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less or equal than zero");
        }

        if(createProductCommand.getTitle() == null
                || createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        //creating event object..
        ProductCreatedEvent productCreatedEvent=new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand,productCreatedEvent);

        AggregateLifecycle.apply(productCreatedEvent);//publishing ProductCreatedEvent event.
        //once you call .apply method, system will dispatch the event to
        // all events handlers inside this aggregate so that the state
        //of this aggregate can be updated with new information
        //Here ProductLookupEventsHandler.java and query/ProductEventHandler.java are event handler
        //will execute when the .apply method called.

        //throw new Exception("Test error");
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand){
        //NOTE that whenever ProductAggregate get load it loads with
        //latest/current state values of product.
        //hence I am comparing quantity with reserveProductCommand.getQuantity()
        if(quantity < reserveProductCommand.getQuantity()){
            throw new IllegalStateException("Insufficient number of items in stock");
        }

        //now raise ProductReservedEvent to update the product quantity
        //so that public void on(ProductReservedEvent productReservedEvent) { > will update
        //quantity in event store
        //and read micro-service i.e query class will capture this event
        //and update the quantity into read database.
        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();

        AggregateLifecycle.apply(productReservedEvent);
    }

    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand){
        ProductReservationCancelledEvent productReservationCancelledEvent =
                ProductReservationCancelledEvent.builder()
                        .orderId(cancelProductReservationCommand.getOrderId())
                        .productId(cancelProductReservationCommand.getProductId())
                        .quantity(cancelProductReservationCommand.getQuantity())
                        .reason(cancelProductReservationCommand.getReason())
                        .userId(cancelProductReservationCommand.getUserId())
                        .build();

        AggregateLifecycle.apply(productReservationCancelledEvent);
    }
    @EventSourcingHandler //this will store event into event store
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.title = productCreatedEvent.getTitle();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity = quantity- productReservedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelledEvent productReservationCancelledEvent){
        this.quantity=this.quantity+ productReservationCancelledEvent.getQuantity();
    }

}
