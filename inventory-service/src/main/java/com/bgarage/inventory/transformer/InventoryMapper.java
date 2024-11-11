package com.bgarage.inventory.transformer;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.bgarage.inventory.dto.PartRequest;
import com.bgarage.inventory.dto.PartResponse;
import com.bgarage.inventory.entity.Part;
import com.bgarage.inventory.events.LowInventoryEvent;

@Mapper
public interface InventoryMapper {
	
	InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);

    Part toPart(PartRequest partRequest);

    PartRequest toPartRequest(Part part);
    
    PartResponse toPartResponse(Part part);
    
    List<PartResponse> toPartResponseList(List<Part> parts);
    
    LowInventoryEvent toLowInventoryEvent(Part part);

}
