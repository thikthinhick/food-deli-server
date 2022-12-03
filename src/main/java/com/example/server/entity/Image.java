package com.example.server.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Image implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;
    private String url;
}
