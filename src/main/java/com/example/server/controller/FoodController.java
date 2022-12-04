package com.example.server.controller;

import com.example.server.entity.Food;
import com.example.server.repository.FoodRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> insertRestaurant(@RequestBody Food food) {
        foodRepository.save(food);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(foodRepository.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getFoodById(@PathVariable("id") Long id) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new NullPointerException("Không tìm thấy món ăn!"));
        return ResponseEntity.ok(food);
    }

}
