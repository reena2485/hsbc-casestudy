package com.bgarage.inventory.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowInventoryEvent {
	
    private String partId;
    
    private String partName;
    
    private int availableQty;
    
    private int thresholdQty;
    
    private int minOrderQty;
    
    private String supplierId;
    
}
