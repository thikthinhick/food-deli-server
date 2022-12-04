package com.example.server.entity.ids;

import javax.persistence.Embeddable;

@Embeddable
public class OrderFoodId {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private Long FoodId;
}
