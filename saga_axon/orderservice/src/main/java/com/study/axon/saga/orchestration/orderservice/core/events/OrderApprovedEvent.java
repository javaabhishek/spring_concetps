package com.study.axon.saga.orchestration.orderservice.core.events;

import com.study.axon.saga.orchestration.orderservice.core.model.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {

	private final String orderId;
	private final OrderStatus orderStatus = OrderStatus.APPROVED;
	
}