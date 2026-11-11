package com.antwonesoftware.springorderfulfillment.controller;

import com.antwonesoftware.springorderfulfillment.dto.CreateOrderRequest;
import com.antwonesoftware.springorderfulfillment.dto.OrderResponse;
import com.antwonesoftware.springorderfulfillment.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request) {

        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<OrderResponse> getOrders() {
        return orderService.getAllOrders();
    }
}