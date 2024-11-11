package com.bgarage.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PartRequest {

    @NotNull(message = "Name is required")
    private String name;

    private String description;

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer availableQty;

    @NotNull(message = "Threshold quantity is required")
    @Min(value = 0, message = "Threshold quantity cannot be negative")
    private Integer thresholdQty;

    @NotNull(message = "Minimum order quantity is required")
    @Min(value = 1, message = "Minimum order quantity must be at least 1")
    private Integer minOrderQty;

    private Long supplierId; 

}