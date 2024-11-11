package com.bgarage.inventory.filter;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

	private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
	private static final String CORRELATION_ID_KEY = "correlationId";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			String correlationId = request.getHeader(CORRELATION_ID_HEADER);
			if (correlationId == null || correlationId.isEmpty()) {
				correlationId = UUID.randomUUID().toString();
			}

			MDC.put(CORRELATION_ID_KEY, correlationId);

			response.setHeader(CORRELATION_ID_HEADER, correlationId);

			filterChain.doFilter(request, response);
		} finally {
			MDC.remove(CORRELATION_ID_KEY);
		}
	}

}
