package com.bgarage.inventory.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "inventory_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class InventoryLog {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "part_id", nullable = false)
    private Long partId; 

    @Column(name = "change_qty", nullable = false)
    private Integer changeQty; 

   // Values can be 'received', 'used', 'adjustment'
    @Column(name = "change_type", length = 50, nullable = false)
    private String changeType; 

    @Column(name = "timestamp", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; 
    
}
