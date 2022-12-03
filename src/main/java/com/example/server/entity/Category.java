package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("categories")
    private List<Food> foods = new ArrayList<>();
}
