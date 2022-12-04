package com.example.server.controller;

import com.example.server.entity.Food;
import com.example.server.entity.Restaurant;
import com.example.server.repository.FoodRepository;
import com.example.server.repository.RestaurantRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(restaurantRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<?> findRestaurantById(@RequestParam("id") Long id) {
        Restaurant restaurant = restaurantRepository
                .findById(id)
                .orElseThrow(() -> new NullPointerException("not find restaurant!"));
        return ResponseEntity.ok(restaurant);
    }

    @PostMapping
    public ResponseEntity<?> insertRestaurant(@RequestBody Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{restaurantId}/food/{foodId}")
    public void addFoodToRestaurant(@PathVariable("restaurantId") Long restaurantId, @PathVariable("foodId") Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new NullPointerException("Not find food"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NullPointerException("Not find restaurant"));
        restaurant.addFood(food);
        restaurantRepository.save(restaurant);
    }

    @GetMapping("/category")
    public ResponseEntity<List<Restaurant>> findRestaurantByCategory(@RequestParam(name = "id", required = false) Long id) {
        List<Restaurant> restaurants;
        if (id == null)
            restaurants = restaurantRepository.findAll();
        else restaurants = restaurantRepository.findRestaurantByCategoryId(id);
        restaurants.forEach(element -> element.setFoods(null));
        return ResponseEntity.ok(restaurants);
    }
}
