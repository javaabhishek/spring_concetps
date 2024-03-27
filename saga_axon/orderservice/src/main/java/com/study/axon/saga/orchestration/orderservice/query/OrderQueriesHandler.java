package com.study.axon.saga.orchestration.orderservice.query;

import com.study.axon.saga.orchestration.orderservice.core.data.OrderEntity;
import com.study.axon.saga.orchestration.orderservice.core.data.OrdersRepository;
import com.study.axon.saga.orchestration.orderservice.core.model.OrderSummary;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class OrderQueriesHandler {
    private final OrdersRepository ordersRepository;

    public OrderQueriesHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(),
                orderEntity.getOrderStatus(), "");
    }
}
