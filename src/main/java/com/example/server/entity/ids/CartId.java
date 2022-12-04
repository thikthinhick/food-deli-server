package com.example.server.entity.ids;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class CartId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long foodId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartId)) return false;
        CartId cartId = (CartId) o;
        return Objects.equals(getFoodId(), cartId.getFoodId()) &&
                Objects.equals(getUserId(), cartId.getUserId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getUserId(), getFoodId());
    }
}
