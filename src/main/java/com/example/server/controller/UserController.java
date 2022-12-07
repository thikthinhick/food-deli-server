package com.example.server.controller;

import com.example.server.entity.Cart;
import com.example.server.entity.Restaurant;
import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.OrderService;
import com.example.server.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository ordersRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @GetMapping("/{userId}/cart")
    public ResponseEntity<?> getCart(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("not find user"));
        List<Cart> carts = user.getCarts()
                .stream().peek(element -> {
                    element.setId(null);
                    element.getFood().setCategories(null);
                }).collect(Collectors.toList());
        Map<Long, List<Cart>> cartGroup =
                carts.stream().collect(Collectors.groupingBy(w -> w.getFood().getRestaurant().getId()));
        List<FormResponseCart> formResponseCarts = new ArrayList<>();
        Set<Long> keys = cartGroup.keySet();
        for (Long key: keys) {
            Restaurant restaurant = cartGroup.get(key).get(0).getFood().getRestaurant();
            restaurant.setFoods(null);
            List<Cart> x = cartGroup.get(key).stream().peek(element -> element.getFood().setRestaurant(null)).collect(Collectors.toList());
            formResponseCarts.add(new FormResponseCart(restaurant, x));
        }
        return ResponseEntity.ok(formResponseCarts);
    }
    @GetMapping("/{userId}/order")
    public ResponseEntity<?> getOrder(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("not find user"));
        return ResponseEntity.ok(ordersRepository.findAll());
    }
    @GetMapping
    public ResponseEntity<?> getUserById(@RequestParam("id") Long id) {
        if(id != null) return ResponseEntity.ok(userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("not find user")));
        return ResponseEntity.badRequest().body("");
    }
    @PutMapping("/{userId}/food/{foodId}/addtocart")
    public void addToCart(@PathVariable("userId") Long userId, @PathVariable("foodId") Long foodId) {
        userService.addFoodToCart(userId, foodId);
    }
    @GetMapping("/{userId}/createorder")
    public void createOrder(@PathVariable("userId") Long userId) {
        orderService.addCartToOrder(userId);
    }

}
@Data
@AllArgsConstructor
class FormResponseCart{
    private Restaurant restaurant;
    private List<Cart> foods;
}
