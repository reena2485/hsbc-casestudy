package com.bgarage.order.service.external;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.bgarage.order.dto.SupplierOrderRequest;
import com.bgarage.order.dto.SupplierOrderResponse;
import com.bgarage.order.entity.Order;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Component
@Slf4j
public class SupplierServiceClient {

	private WebClient webClient;

	private String supplierServiceOrderPlaceUrl;

	private String supplierServiceOrderStatusCheckUrl;

	@Autowired
	public SupplierServiceClient(WebClient.Builder webClientBuilder,
			@Value("${application.order.supplierServiceEndpoint}") String supplierServiceBaseUrl,
			@Value("${application.order.supplierServiceOrderPlaceUrl:/suppliers/{supplierId}/place-order}") String supplierServiceOrderPlaceUrl,
			@Value("${application.order.supplierServiceOrderStatusCheckUrl:/suppliers/{supplierId}/orders/{orderId}}") String supplierServiceOrderStatusCheckUrl) {
		this.webClient = webClientBuilder.baseUrl(supplierServiceBaseUrl).build();
		this.supplierServiceOrderPlaceUrl = supplierServiceOrderPlaceUrl;
		this.supplierServiceOrderStatusCheckUrl = supplierServiceOrderStatusCheckUrl;
	}

	public Mono<Void> placeOrder(Order order) {
		SupplierOrderRequest request = new SupplierOrderRequest(order.getId(), order.getPartId(), order.getQuantity(),
				order.getCreatedAt());
		return webClient.post().uri(supplierServiceOrderPlaceUrl, order.getSupplierId()).bodyValue(request).retrieve()
				.bodyToMono(Void.class).retryWhen(getRetrySpec())
				.doOnSubscribe(subscription -> log.info("Placing order for part ID: {} with supplier ID: {}",
						order.getPartId(), order.getSupplierId()))
				.doOnError(ex -> log.error("Failed to place order for part ID: {}. Error: {}", order.getPartId(),
						ex.getMessage()));
	}

	public Mono<SupplierOrderResponse> getOrderStatus(String supplierId, Long orderId) {
		return webClient.get().uri(supplierServiceOrderStatusCheckUrl, supplierId, orderId).retrieve()
				.bodyToMono(SupplierOrderResponse.class).retryWhen(getRetrySpec())
				.doOnSubscribe(subscription -> log.info("Fetching order status for order ID: {} from supplier ID: {}",
						orderId, supplierId))
				.doOnError(ex -> log.error("Failed to retrieve order status for order ID: {}. Error: {}", orderId,
						ex.getMessage()));
	}

	private Retry getRetrySpec() {
		return Retry.fixedDelay(3, Duration.ofSeconds(2)) // 3 retries with 2-second delay
				.filter(throwable -> throwable instanceof WebClientResponseException)
				.doBeforeRetry(retrySignal -> log.info("Retrying... Attempt: " + (retrySignal.totalRetries() + 1)));
	}

}
