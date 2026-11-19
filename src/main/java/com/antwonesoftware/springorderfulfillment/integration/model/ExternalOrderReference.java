package com.antwonesoftware.springorderfulfillment.integration.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "external_order_references",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_external_order_source_id",
                columnNames = {"source_system", "external_order_id"}
        )
)
public class ExternalOrderReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_system", nullable = false)
    private String sourceSystem;

    @Column(name = "external_order_id", nullable = false)
    private String externalOrderId;

    @Column(name = "request_hash", nullable = false, length = 64)
    private String requestHash;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected ExternalOrderReference() {
    }

    public ExternalOrderReference(
            String sourceSystem,
            String externalOrderId,
            String requestHash,
            Long orderId) {

        this.sourceSystem = sourceSystem;
        this.externalOrderId = externalOrderId;
        this.requestHash = requestHash;
        this.orderId = orderId;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public String getExternalOrderId() {
        return externalOrderId;
    }

    public String getRequestHash() {
        return requestHash;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}