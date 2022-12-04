package com.example.server.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {
    public enum Role {
        ROLE_ADMIN,
        ROLE_USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String fullName;
    private String password;
    private Role role;
    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Address> addresses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();
    public void addAddress(Address address) {
        this.addresses.add(address);
        address.setUser(this);
    }
    public void removeAddress(Long addressId) {
        Address address = addresses.stream().filter(element -> element.getId() == addressId).findFirst().orElse(null);
        if(address != null)
            this.getAddresses().remove(address);
    }
}