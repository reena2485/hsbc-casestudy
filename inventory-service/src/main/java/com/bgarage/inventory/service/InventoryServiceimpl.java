package com.bgarage.inventory.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bgarage.inventory.dto.PartRequest;
import com.bgarage.inventory.dto.PartResponse;
import com.bgarage.inventory.entity.InventoryLog;
import com.bgarage.inventory.entity.Part;
import com.bgarage.inventory.events.publisher.InventoryEventPublisher;
import com.bgarage.inventory.repository.InventoryLogRepository;
import com.bgarage.inventory.repository.PartRepository;
import com.bgarage.inventory.transformer.InventoryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceimpl implements InventoryService {

	private final PartRepository partRepository;

	private final InventoryLogRepository inventoryLogRepository;

	private final InventoryEventPublisher inventoryEventPublisher;

	private final InventoryMapper inventoryMapper = InventoryMapper.INSTANCE;

	@Value("${application.inventory.lowInventoryThreshold:10}")
	private final Integer lowInventoryThreshold;

	@Transactional
	public PartResponse addOrUpdatePart(PartRequest partRequest) {
		Part part = inventoryMapper.toPart(partRequest);
		part.setUpdatedAt(LocalDateTime.now());
		return inventoryMapper.toPartResponse(partRepository.save(part));
	}

	@Transactional
	public PartResponse adjustInventory(Long partRequestId, Integer changeQty, String changeType, String notes) {
		Part part = partRepository.findById(partRequestId)
				.orElseThrow(() -> new IllegalArgumentException("PartRequest not found"));

		part.setAvailableQty(part.getAvailableQty() + changeQty);
		part.setUpdatedAt(LocalDateTime.now());
		partRepository.save(part);

		InventoryLog inventoryLog = new InventoryLog();
		inventoryLog.setPartId(partRequestId);
		inventoryLog.setChangeQty(changeQty);
		inventoryLog.setChangeType(changeType);
		inventoryLog.setNotes(notes);
		inventoryLogRepository.save(inventoryLog);

		checkLowInventory(part);
		return inventoryMapper.toPartResponse(part);
	}

	public List<PartResponse> getLowInventoryParts() {
		return inventoryMapper.toPartResponseList(partRepository.findByAvailableQtyLessThan(lowInventoryThreshold));
	}

	private void checkLowInventory(Part part) {
		if (part.getAvailableQty() < part.getThresholdQty()) {
			inventoryEventPublisher.publishLowInventoryEvent(inventoryMapper.toLowInventoryEvent(part));
		}
	}

}
