package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JsonIgnoreProperties("restaurant")
    private List<Food> foods = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;
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
