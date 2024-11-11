package com.bgarage.inventory.service;

import java.util.List;

import com.bgarage.inventory.dto.PartRequest;
import com.bgarage.inventory.dto.PartResponse;

public interface InventoryService {

	PartResponse addOrUpdatePart(PartRequest partRequest);
	
	PartResponse adjustInventory(Long id, Integer quantity, String changeType, String notes);
	
	List<PartResponse> getLowInventoryParts();
	
}
