package com.example.server.controller;

import com.example.server.entity.Food;
import com.example.server.entity.Restaurant;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.FoodRepository;
import com.example.server.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/restaurant")
public class RestaurantController {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private FoodRepository foodRepository;

    @GetMapping
    public ResponseEntity<?> findRestaurantById(@RequestParam(name = "id", required = false) Long id) {
        if(id != null) {
            Restaurant restaurant = restaurantRepository
                    .findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("not find restaurant!"));
            return ResponseEntity.ok(restaurant);
        }

        return ResponseEntity.ok(restaurantRepository.findAll()
                .stream().map(element -> {element.setFoods(null);
                    return element;}).collect(Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<?> insertRestaurant(@RequestBody Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{restaurantId}/food/{foodId}")
    public void addFoodToRestaurant(@PathVariable("restaurantId") Long restaurantId, @PathVariable("foodId") Long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Not find food"));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new ResourceNotFoundException("Not find restaurant"));
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
