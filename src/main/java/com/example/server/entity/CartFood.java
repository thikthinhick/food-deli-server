package com.example.server.entity;

import com.example.server.entity.ids.CartFoodId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class CartFood {
    @EmbeddedId
    private CartFoodId id = new CartFoodId();
    private int amount;
    @ManyToOne
    @MapsId("cartId")
    private Cart cart;

    @ManyToOne
    @MapsId("foodId")
    private Food food;
}
