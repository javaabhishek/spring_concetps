package com.study.axon.saga.orchestration.orderservice.query;

import lombok.Value;

@Value
public class FindOrderQuery {

	private final String orderId;
	
}