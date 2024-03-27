package com.study.axon.saga.orchestration.orderservice.query;

import com.study.axon.saga.orchestration.orderservice.core.data.OrderEntity;
import com.study.axon.saga.orchestration.orderservice.core.data.OrdersRepository;
import com.study.axon.saga.orchestration.orderservice.core.events.OrderApprovedEvent;
import com.study.axon.saga.orchestration.orderservice.core.events.OrderCreatedEvent;
import com.study.axon.saga.orchestration.orderservice.core.events.OrderRejectedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {

    private final OrdersRepository ordersRepository;

    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);

        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent){
        OrderEntity orderEntity=ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());
        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectedEvent orderRejectedEvent){
        OrderEntity orderEntity=ordersRepository.findByOrderId(orderRejectedEvent.getOrderId());
        orderEntity.setOrderStatus(orderRejectedEvent.getOrderStatus());
        ordersRepository.save(orderEntity);
    }
}
