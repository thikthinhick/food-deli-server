package com.example.server.controller;

import com.example.server.entity.Address;
import com.example.server.entity.Restaurant;
import com.example.server.entity.User;
import com.example.server.repository.AddressRepository;
import com.example.server.repository.RestaurantRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private AddressRepository addressRepository;
    @PutMapping("/user/{userId}/address")
    public ResponseEntity<?> addAddressToUser(@RequestBody Address address, @PathVariable("userId") Long id) {
        if (!userRepository.existsById(id)) return ResponseEntity.badRequest().body("Not find user");
        Address add = addressRepository.save(address);
        User user = userRepository.findById(id).orElseThrow(() -> new NullPointerException("Not find user"));
        user.addAddress(add);
        userRepository.save(user);
        return ResponseEntity.ok("add address to user success");
    }
    @PutMapping("/restaurant/{restaurantId}/address")
    public ResponseEntity<?> addAddressToRestaurant(@RequestBody Address address, @PathVariable("restaurantId") Long id) {
        if (!restaurantRepository.existsById(id)) return ResponseEntity.badRequest().body("Not find restaurant");
        Address add = addressRepository.save(address);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new NullPointerException("Not find restaurant"));
        restaurant.setAddress(add);
        restaurantRepository.save(restaurant);
        return ResponseEntity.ok("add address to restaurant success");
    }
    @GetMapping("/test")
    public List<User> getAll() {
        return userRepository.findAll();
    }
}
