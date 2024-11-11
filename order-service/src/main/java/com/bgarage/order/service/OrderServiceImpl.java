package com.bgarage.order.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bgarage.order.constants.OrderStatus;
import com.bgarage.order.dto.OrderRequest;
import com.bgarage.order.dto.SupplierOrderResponse;
import com.bgarage.order.entity.Order;
import com.bgarage.order.events.OrderStatusUpdatedEvent;
import com.bgarage.order.events.publisher.OrderEventPublisher;
import com.bgarage.order.repository.OrderRepository;
import com.bgarage.order.scheduler.OrderScheduler;
import com.bgarage.order.service.external.SupplierServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final SupplierServiceClient supplierServiceClient;

	private final OrderScheduler orderScheduler;

	private final OrderEventPublisher orderEventPublisher;

	@Value("${application.order.supplierA:SUPPLIER-A}")
	private final String supplierA;

	@Value("${application.order.supplierB:SUPPLIER-B}")
	private final String supplierB;

	@Override
	public void processLowInventory(OrderRequest orderRequest) {
		if (supplierA.equals(orderRequest.getSupplierId())) {
			placeOrder(orderRequest);
		} else if (supplierB.equals(orderRequest.getSupplierId())) {
			orderScheduler.scheduleOrder(orderRequest);
		}

	}

	@Override
	public void placeOrder(OrderRequest orderRequest) {
		Order order = Order.builder().partId(orderRequest.getPartId()).quantity(orderRequest.getQuantity())
				.orderStatus(OrderStatus.PENDING).supplierId(orderRequest.getSupplierId()).build();
		Order savedOrder = orderRepository.save(order);

		supplierServiceClient.placeOrder(savedOrder);

		SupplierOrderResponse supplierOrderResponse = supplierServiceClient
				.getOrderStatus(savedOrder.getSupplierId(), savedOrder.getId()).block();

		updateOrderStatus(savedOrder, OrderStatus.valueOf(supplierOrderResponse.getStatus()));

		OrderStatusUpdatedEvent orderStatusUpdatedEvent = OrderStatusUpdatedEvent.builder().orderId(order.getId())
				.partId(order.getPartId()).status(supplierOrderResponse.getStatus()).supplierId(order.getSupplierId())
				.expectedDelivery(order.getExpectedDeliveryDate()).build();

		orderEventPublisher.publishOrderStatusUpdate(orderStatusUpdatedEvent);

	}

	private void updateOrderStatus(Order order, OrderStatus status) {
		order.setOrderStatus(status);
		orderRepository.save(order);
	}

}
