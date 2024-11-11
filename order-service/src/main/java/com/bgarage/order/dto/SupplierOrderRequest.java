package com.bgarage.order.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SupplierOrderRequest {
	
	private Long orderId;
	
	private String partId;
	
	private int quantity;
	
	private LocalDateTime orderDate;
	
}