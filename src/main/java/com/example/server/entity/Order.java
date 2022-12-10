package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties("user")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String code;
    @OneToMany(mappedBy = "order")
    @JsonIgnoreProperties("order")
    private List<OrderFood> orderFoods = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("order")
    private List<OrderStatus> statuses = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String address;
    private Long price;
    public Long getId() {
        return id;
    }
    public List<OrderFood> getOrderFoods() {
        return orderFoods;
    }

    public void setOrderFoods(List<OrderFood> orderFoods) {
        this.orderFoods = orderFoods;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<OrderStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<OrderStatus> statuses) {
        this.statuses = statuses;
    }


    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
