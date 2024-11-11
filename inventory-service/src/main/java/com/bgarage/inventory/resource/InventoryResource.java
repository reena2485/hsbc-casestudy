package com.bgarage.inventory.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bgarage.inventory.dto.PartRequest;
import com.bgarage.inventory.dto.PartResponse;
import com.bgarage.inventory.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryResource {
	
    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/parts")
    public PartResponse addOrUpdatePart(@RequestBody PartRequest partRequest) {
        return inventoryService.addOrUpdatePart(partRequest);
    }

    @PatchMapping("/parts/{id}/adjust-quantity")
    public PartResponse adjustInventory(@PathVariable Long id, @RequestParam Integer quantity,
                                @RequestParam String changeType, @RequestParam String notes) {
        return inventoryService.adjustInventory(id, quantity, changeType, notes);
    }

    @GetMapping("/parts/low-inventory")
    public List<PartResponse> getLowInventoryParts() {
        return inventoryService.getLowInventoryParts();
    }
    
}
