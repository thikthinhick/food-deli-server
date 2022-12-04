package com.example.server.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String fullName;
    private String password;
    private Role role;
    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new HashSet<>();
}