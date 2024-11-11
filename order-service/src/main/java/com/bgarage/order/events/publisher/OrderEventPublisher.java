package com.bgarage.order.events.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Component;

import com.bgarage.order.events.OrderStatusUpdatedEvent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OrderEventPublisher {

	private final ReactiveKafkaProducerTemplate<String, OrderStatusUpdatedEvent> kafkaProducerTemplate;

	private final String topic;

	public OrderEventPublisher(ReactiveKafkaProducerTemplate<String, OrderStatusUpdatedEvent> kafkaProducerTemplate,
			@Value("${spring.kafka.producer.topic}") String topic) {
		this.kafkaProducerTemplate = kafkaProducerTemplate;
		this.topic = topic;
	}

	public Mono<Void> publishOrderStatusUpdate(OrderStatusUpdatedEvent event) {

		return kafkaProducerTemplate.send(topic, String.valueOf(event.getOrderId()), event).doOnSuccess(result -> log
				.info("Order status update event published successfully for Order ID: {}. Topic: {}, Partition: {}, Offset: {}",
						event.getOrderId(), result.recordMetadata().topic(), result.recordMetadata().partition(),
						result.recordMetadata().offset()))
				.doOnError(ex -> log.error("Failed to publish order status update event for Order ID: {}. Error: {}",
						event.getOrderId(), ex.getMessage()))
				.then(); // Convert to Mono<Void> to indicate completion
	}

}
