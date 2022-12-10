package com.example.server.entity.ids;


import com.example.server.entity.Order;
import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Data
public class OrderStatusId implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private Long statusId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderStatusId)) return false;
        OrderStatusId orderStatusId = (OrderStatusId) o;
        return Objects.equals(getOrderId(), orderStatusId.getOrderId()) &&
                Objects.equals(getStatusId(), orderStatusId.getStatusId());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getOrderId(), getStatusId());
    }
}
