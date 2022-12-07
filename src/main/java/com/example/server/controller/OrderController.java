package com.example.server.controller;

import com.example.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/orders")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/{userId}")
    public void createOrders(@PathVariable("userId") Long userId) {

        orderService.addCartToOrder(userId);
    }
}
