package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Restaurant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String backgroundImage;
    @OneToMany(mappedBy = "restaurant")
    @JsonManagedReference
    private List<Food> foods = new ArrayList<>();
    public void addFood(Food food) {
        this.foods.add(food);
        food.setRestaurant(this);
    }
    public void deleteFood(Long foodId) {
        Food food = foods.stream().filter(item -> item.getId() == foodId).findFirst().orElse(null);
        if(food != null) {
            this.foods.remove(food);
            food.setRestaurant(null);
        }
    }
}