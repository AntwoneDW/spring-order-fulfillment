package com.antwonesoftware.springorderfulfillment.dto;

import com.antwonesoftware.springorderfulfillment.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        Long customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}