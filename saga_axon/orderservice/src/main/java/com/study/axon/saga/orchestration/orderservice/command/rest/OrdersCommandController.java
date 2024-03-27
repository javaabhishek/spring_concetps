package com.study.axon.saga.orchestration.orderservice.command.rest;

import com.study.axon.saga.orchestration.orderservice.command.commands.CreateOrderCommand;
import com.study.axon.saga.orchestration.orderservice.core.model.OrderStatus;
import com.study.axon.saga.orchestration.orderservice.core.model.OrderSummary;
import com.study.axon.saga.orchestration.orderservice.query.FindOrderQuery;
import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public OrdersCommandController(CommandGateway commandGateway,
                                   QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway=queryGateway;
    }

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody OrderCreateRest order) {

        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";
        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = CreateOrderCommand.builder().addressId(order.getAddressId())
                .productId(order.getProductId()).userId(userId).quantity(order.getQuantity()).orderId(orderId)
                .orderStatus(OrderStatus.CREATED).build();

        FindOrderQuery findOrderQuery=new FindOrderQuery(orderId);

        SubscriptionQueryResult<OrderSummary, OrderSummary> result=queryGateway.subscriptionQuery(findOrderQuery,
                ResponseTypes.instanceOf(OrderSummary.class)
        ,ResponseTypes.instanceOf(OrderSummary.class));

        try{
            commandGateway.sendAndWait(createOrderCommand);
            return result.updates().blockFirst();
        }finally {
            result.close();
        }

    }
}
