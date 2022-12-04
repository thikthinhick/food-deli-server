package com.example.server.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Food implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Long price;
    private Long oldPrice;

    @ManyToMany
    @JoinTable(name = "food_category",
            joinColumns = @JoinColumn(name = "food_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnoreProperties("foods")
    private List<Category> categories = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    @JsonIgnoreProperties("foods")
    private Restaurant restaurant;
    @OneToMany(mappedBy = "food")
    private Set<Image> images = new HashSet<>();
    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Cart> carts = new ArrayList<>();
}
