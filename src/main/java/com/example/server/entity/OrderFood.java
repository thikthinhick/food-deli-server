package com.example.server.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderFood {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int amount;
    @ManyToOne
    @JoinColumn(name = "food_id")
    private Food food;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
