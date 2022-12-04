package com.example.server.entity;

import javax.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
