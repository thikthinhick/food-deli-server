package com.example.server.controller;

import com.example.server.entity.Cart;
import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/{userId}/cart")
    public ResponseEntity<?> getCart(@PathVariable("userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("not find user"));
        List<Cart> carts = user.getCarts().stream().map(element -> {
            element.setId(null);
            element.getFood().setCategories(null);
            return element;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(carts);
    }
}
