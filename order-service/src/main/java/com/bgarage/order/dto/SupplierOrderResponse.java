package com.bgarage.order.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SupplierOrderResponse {
	
    private Long orderId;
    
    private String status;
    
    private LocalDateTime expectedDeliveryDate;
    
}
