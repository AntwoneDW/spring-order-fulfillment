package com.antwonesoftware.springorderfulfillment.repository;


import com.antwonesoftware.springorderfulfillment.model.Order;
import com.antwonesoftware.springorderfulfillment.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByStatus(OrderStatus status);
}