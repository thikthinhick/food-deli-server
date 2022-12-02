package com.example.server.entity;

import javax.persistence.*;

import lombok.Data;

@Entity
@Table(name = "user")
@Data // lombok
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
}