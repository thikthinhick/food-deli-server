package com.example.server.repository;

import com.example.server.entity.OrderStatus;
import com.example.server.entity.ids.OrderStatusId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, OrderStatusId> {
}
