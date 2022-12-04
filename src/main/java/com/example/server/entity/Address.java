package com.example.server.entity;

import javax.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String province;
    private String district;
    private String detail;
    private double longitude;
    private double latitude;
    private boolean active;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
