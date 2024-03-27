package com.study.axon.saga.orchestration.orderservice.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrderEntity, String> {
	OrderEntity findByOrderId(String orderId);
}