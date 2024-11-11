package com.bgarage.inventory.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PartResponse {

    private Long partId;
    
    private String name;
    
    private String description;
    
    private Integer availableQty;
    
    private Integer thresholdQty;
    
    private Integer minOrderQty;
    
    private Long supplierId;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
}
