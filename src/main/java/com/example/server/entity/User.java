package com.example.server.entity;

import javax.persistence.*;
import lombok.Data;


import java.io.Serializable;

@Entity
@Table(name = "user")
@Data // lombok
public class User implements Serializable {
    public enum Role{
        ROLE_ADMIN,
        ROLE_USER
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String fullName;
    private String password;
    private Role role;
}