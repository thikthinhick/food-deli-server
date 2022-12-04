package com.example.server.entity.ids;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class UserFoodId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long foodId;
}
