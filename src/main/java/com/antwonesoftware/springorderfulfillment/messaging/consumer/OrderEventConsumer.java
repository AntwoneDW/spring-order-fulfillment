package com.antwonesoftware.springorderfulfillment.messaging.consumer;

import com.antwonesoftware.springorderfulfillment.messaging.config.KafkaTopicConfig;
import com.antwonesoftware.springorderfulfillment.messaging.events.OrderConfirmedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;

import org.springframework.messaging.handler.annotation.Header;

import org.springframework.stereotype.Component;


@Component
public class OrderEventConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(OrderEventConsumer.class);


    @KafkaListener(
            topics = KafkaTopicConfig.ORDER_EVENTS_TOPIC,
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleOrderConfirmed(
            OrderConfirmedEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {

        log.info(
                "Consumed OrderConfirmed event: key={}, orderId={}, customerId={}, amount={}",
                key,
                event.orderId(),
                event.customerId(),
                event.totalAmount()
        );
    }
}