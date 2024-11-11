package com.bgarage.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bgarage.inventory.entity.Part;

public interface PartRepository extends JpaRepository<Part, Long>  {

	List<Part> findByAvailableQtyLessThan(Integer thresholdQty);
	
}
