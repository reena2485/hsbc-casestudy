package com.bgarage.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bgarage.inventory.entity.InventoryLog;

public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
	
}