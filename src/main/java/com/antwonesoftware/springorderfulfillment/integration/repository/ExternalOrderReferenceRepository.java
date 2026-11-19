package com.antwonesoftware.springorderfulfillment.integration.repository;

import com.antwonesoftware.springorderfulfillment.integration.model.ExternalOrderReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExternalOrderReferenceRepository
        extends JpaRepository<ExternalOrderReference, Long> {

    Optional<ExternalOrderReference>
    findBySourceSystemAndExternalOrderId(
            String sourceSystem,
            String externalOrderId);
}