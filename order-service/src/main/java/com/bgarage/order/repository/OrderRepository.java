package com.bgarage.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bgarage.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	
	// List<Order> findByOrderStatus(Order.OrderStatus orderStatus);
	
}
