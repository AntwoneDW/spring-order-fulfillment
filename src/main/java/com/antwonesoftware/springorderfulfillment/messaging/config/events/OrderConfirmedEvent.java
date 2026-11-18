package com.antwonesoftware.springorderfulfillment.messaging.events;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderConfirmedEvent(
        Long orderId,
        Long customerId,
        BigDecimal totalAmount,
        Instant occurredAt
) {
}