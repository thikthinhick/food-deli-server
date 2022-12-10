package com.example.server.entity;

import com.example.server.entity.ids.OrderStatusId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Data
@JsonIgnoreProperties({"id", "order "})
public class OrderStatus {
    @EmbeddedId
    private OrderStatusId id = new OrderStatusId();
    private Timestamp time;
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
    @ManyToOne
    @MapsId("statusId")
    @JoinColumn(name = "status_id")
    private Status status;
}
