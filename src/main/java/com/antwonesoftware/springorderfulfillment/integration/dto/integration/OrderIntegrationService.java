package com.antwonesoftware.springorderfulfillment.integration;

import com.antwonesoftware.springorderfulfillment.dto.CreateOrderRequest;
import com.antwonesoftware.springorderfulfillment.dto.OrderResponse;
import com.antwonesoftware.springorderfulfillment.integration.dto.ExternalOrderRequest;
import com.antwonesoftware.springorderfulfillment.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderIntegrationService {

    private final OrderService orderService;

    public OrderIntegrationService(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderResponse ingest(ExternalOrderRequest request) {

        CreateOrderRequest internalRequest = new CreateOrderRequest(
                request.getCustomerId(),
                request.getTotalAmount()
        );

        return orderService.createOrder(internalRequest);
    }
}