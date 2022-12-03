package com.example.server.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
public class Food implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Long price;
    private Long oldPrice;
    @ManyToMany
    @JoinTable(name = "food_category",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnoreProperties("foods")
    private List<Category> categories = new ArrayList<>();
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @OneToMany(mappedBy = "food")
    private Set<Image> images = new HashSet<>();
}
