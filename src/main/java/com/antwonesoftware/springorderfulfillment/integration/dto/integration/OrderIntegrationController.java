package com.antwonesoftware.springorderfulfillment.integration;

import com.antwonesoftware.springorderfulfillment.dto.OrderResponse;
import com.antwonesoftware.springorderfulfillment.integration.dto.ExternalOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/integrations/orders")
public class OrderIntegrationController {

    private final OrderIntegrationService integrationService;

    public OrderIntegrationController(OrderIntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    @PostMapping(
            consumes = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OrderResponse> ingestOrder(
            @Valid @RequestBody ExternalOrderRequest request) {

        OrderResponse response = integrationService.ingest(request);

        return ResponseEntity.status(201).body(response);
    }
}