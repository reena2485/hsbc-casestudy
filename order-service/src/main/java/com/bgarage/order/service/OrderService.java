package com.bgarage.order.service;

import com.bgarage.order.dto.OrderRequest;

public interface OrderService {

	public void processLowInventory(OrderRequest orderRequest);
	
	public void placeOrder(OrderRequest orderRequest);
	
}
