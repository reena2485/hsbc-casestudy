package com.bgarage.order.scheduler;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.Queue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bgarage.order.dto.OrderRequest;
import com.bgarage.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderScheduler {

	private Queue<OrderRequest> orderQueue = new LinkedList<>();

	private final OrderService orderService;

	public void scheduleOrder(OrderRequest orderRequest) {
		orderQueue.add(orderRequest); // Add to queue
	}

	// Scheduled task to process orders between 12:00 AM and 1:00 AM
	@Scheduled(cron = "0 0 0 * * ?")
	public void processScheduledOrders() {
		LocalTime now = LocalTime.now();
		LocalTime discountStart = LocalTime.MIDNIGHT;
		LocalTime discountEnd = LocalTime.of(1, 0);

		if (now.isAfter(discountStart) && now.isBefore(discountEnd)) {
			while (!orderQueue.isEmpty()) {
				OrderRequest orderRequest = orderQueue.poll();
				orderService.placeOrder(orderRequest);
			}
		}
	}

}
