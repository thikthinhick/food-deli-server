package com.example.server.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@Data // lombok
public class User {
    public enum Role{
        ROLE_ADMIN,
        ROLE_USER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String fullName;
    private String password;
    private Role role;
}