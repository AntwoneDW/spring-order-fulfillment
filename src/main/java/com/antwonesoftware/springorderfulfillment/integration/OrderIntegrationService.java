package com.antwonesoftware.springorderfulfillment.integration;

import com.antwonesoftware.springorderfulfillment.dto.CreateOrderRequest;
import com.antwonesoftware.springorderfulfillment.dto.OrderResponse;
import com.antwonesoftware.springorderfulfillment.exception.IdempotencyConflictException;
import com.antwonesoftware.springorderfulfillment.integration.dto.ExternalOrderRequest;
import com.antwonesoftware.springorderfulfillment.integration.dto.OrderIngestionResult;
import com.antwonesoftware.springorderfulfillment.integration.model.ExternalOrderReference;
import com.antwonesoftware.springorderfulfillment.integration.repository.ExternalOrderReferenceRepository;
import com.antwonesoftware.springorderfulfillment.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@Service
public class OrderIntegrationService {

    private final OrderService orderService;
    private final ExternalOrderReferenceRepository referenceRepository;

    public OrderIntegrationService(
            OrderService orderService,
            ExternalOrderReferenceRepository referenceRepository) {

        this.orderService = orderService;
        this.referenceRepository = referenceRepository;
    }

    @Transactional
    public OrderIngestionResult ingest(ExternalOrderRequest request) {

        String requestHash = calculateRequestHash(request);

        var existingReference =
                referenceRepository.findBySourceSystemAndExternalOrderId(
                        request.getSourceSystem(),
                        request.getExternalOrderId()
                );

        if (existingReference.isPresent()) {

            ExternalOrderReference existing = existingReference.get();

            if (!existing.getRequestHash().equals(requestHash)) {
                throw new IdempotencyConflictException(
                        "External order ID was already used with a different payload"
                );
            }

            OrderResponse existingOrder =
                    orderService.getOrder(existing.getOrderId());

            return new OrderIngestionResult(existingOrder, false);
        }

        CreateOrderRequest internalRequest = new CreateOrderRequest(
                request.getCustomerId(),
                request.getTotalAmount()
        );

        OrderResponse createdOrder =
                orderService.createOrder(internalRequest);

        ExternalOrderReference reference =
                new ExternalOrderReference(
                        request.getSourceSystem(),
                        request.getExternalOrderId(),
                        requestHash,
                        createdOrder.id()
                        // If OrderResponse is a normal class rather than
                        // a record in your project, use getId().
                );

        referenceRepository.saveAndFlush(reference);

        return new OrderIngestionResult(createdOrder, true);
    }

    private String calculateRequestHash(ExternalOrderRequest request) {

        String canonicalPayload =
                request.getSourceSystem() + "|" +
                        request.getExternalOrderId() + "|" +
                        request.getCustomerId() + "|" +
                        request.getTotalAmount()
                                .stripTrailingZeros()
                                .toPlainString();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    canonicalPayload.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable",
                    ex
            );
        }
    }
}