package com.antwonesoftware.springorderfulfillment.integration.dto;

import com.antwonesoftware.springorderfulfillment.dto.OrderResponse;

public record OrderIngestionResult(
        OrderResponse order,
        boolean created
) {
}