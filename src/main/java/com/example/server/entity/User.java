package com.example.server.entity;

import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Table(name = "user")
public class User {
    enum Role {
        ADMIN,
        USER
    }

    @Id
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String username;
    private String password;
    private Role role;
    private String phone;
}
