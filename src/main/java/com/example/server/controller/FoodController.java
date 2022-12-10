package com.example.server.controller;

import com.example.server.entity.Category;
import com.example.server.entity.Food;
import com.example.server.entity.Image;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.CategoryRepository;
import com.example.server.repository.FoodRepository;
import com.example.server.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserService userService;
    @PostMapping
    public ResponseEntity<?> insertRestaurant(@RequestBody Food food) {
        foodRepository.save(food);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping
    public ResponseEntity<?> getFoodById(@RequestParam(name = "id", required = false) Long id) {
        if(id != null) {
            return ResponseEntity.ok(foodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not find food")));
        }
        return ResponseEntity.ok(foodRepository.findAll().stream().map(element -> {element.setRestaurant(null); return element;}).collect(Collectors.toList()));
    }
    @GetMapping("/category")
    public ResponseEntity<?> getListFoodByCategory(@RequestParam(name = "id") Long id) {
        if(id != null) {
            return ResponseEntity.ok(foodRepository.findFoodByCategoryId(id));
        }
        return ResponseEntity.ok(foodRepository.findAll());
    }
    @PutMapping("/{foodId}/category/{categoryId}")
    public void addCategoryToFood(@PathVariable("foodId") Long foodId, @PathVariable("categoryId") Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thể loại"));
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new ResourceNotFoundException("Khong tim thay mon an"));
        List<Category> categoryList = food.getCategories();
        categoryList.add(category);
        food.setCategories(categoryList);
        foodRepository.save(food);
    }
}
@Data
class FoodResponse{
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long oldPrice;
    private Long rate;
    private String restaurant;
    List<Image> images;
}
