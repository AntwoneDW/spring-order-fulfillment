package com.antwonesoftware.springorderfulfillment.messaging.producer;

import com.antwonesoftware.springorderfulfillment.messaging.config.KafkaTopicConfig;
import com.antwonesoftware.springorderfulfillment.messaging.events.OrderConfirmedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Component;

import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
public class OrderEventPublisher {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate<Object, Object> kafkaTemplate;

    public OrderEventPublisher(
            KafkaTemplate<Object, Object> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }


    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void publishOrderConfirmed(
            OrderConfirmedEvent event
    ) {

        String key = event.orderId().toString();

        kafkaTemplate
                .send(
                        KafkaTopicConfig.ORDER_EVENTS_TOPIC,
                        key,
                        event
                )
                .whenComplete((result, exception) -> {

                    if (exception != null) {

                        log.error(
                                "Failed to publish OrderConfirmed event for order {}",
                                event.orderId(),
                                exception
                        );

                        return;
                    }

                    log.info(
                            "Published OrderConfirmed event: orderId={}, partition={}, offset={}",
                            event.orderId(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });
    }
}