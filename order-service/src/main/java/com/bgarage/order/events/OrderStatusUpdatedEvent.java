package com.bgarage.order.events;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class OrderStatusUpdatedEvent {

	private Long orderId;

	private String partId;

	private String status;

	private String supplierId;

	private LocalDate expectedDelivery;

}
