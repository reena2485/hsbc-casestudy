package com.bgarage.inventory.events.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.bgarage.inventory.events.LowInventoryEvent;

@Component
public class InventoryEventPublisher {

	private static final String TOPIC = "low-inventory";

	@Autowired
	private KafkaTemplate<String, LowInventoryEvent> kafkaTemplate;

	public void publishLowInventoryEvent(LowInventoryEvent part) {
		String partitionKey = part.getPartId().toString(); 
		kafkaTemplate.send(TOPIC, partitionKey, part);
	}

}
