package com.example.server.entity;

import com.example.server.entity.ids.UserFoodId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cart {
    @EmbeddedId
    private UserFoodId id = new UserFoodId();
    private int amount;
    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @ManyToOne
    @MapsId("foodId")
    @JoinColumn(name = "food_id")
    private Food food;
}
