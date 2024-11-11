package com.bgarage.order.events.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.bgarage.order.dto.OrderRequest;
import com.bgarage.order.events.LowInventoryEvent;
import com.bgarage.order.service.OrderService;

@Component
public class OrderEventListener {

	private final OrderService orderService;

	@Autowired
	public OrderEventListener(OrderService orderService) {
		this.orderService = orderService;
	}

	@KafkaListener(topics = "#{@environment.getProperty('spring.kafka.topic.low-inventory')}", groupId = "#{@environment.getProperty('spring.kafka.consumer.group-id')}")
	public void onLowInventoryEvent(LowInventoryEvent event) {
		OrderRequest orderRequest = new OrderRequest(event.getPartId(), event.getMinOrderQty(), event.getSupplierId());
		orderService.processLowInventory(orderRequest);
	}
}
