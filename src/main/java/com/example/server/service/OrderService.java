package com.example.server.service;

import com.example.server.entity.Order;
import com.example.server.entity.OrderStatus;
import com.example.server.entity.Status;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.OrderStatusRepository;
import com.example.server.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    public void changeStatusOrder(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Not find orrder"));
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(order);
        Long statusId = order.getStatuses().get(order.getStatuses().size() - 1).getStatus().getId();
        Status status = statusRepository.findById(statusId + 1).orElseThrow(() -> new ResourceNotFoundException("not find"));
        orderStatus.setStatus(status);
        orderStatus.setTime(new Timestamp(System.currentTimeMillis()));
        orderStatusRepository.save(orderStatus);
    }
}
