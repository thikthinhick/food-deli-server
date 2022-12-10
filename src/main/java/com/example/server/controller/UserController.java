package com.example.server.controller;

import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.AddressRepository;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository ordersRepository;

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private OrderService orderService;

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

    @GetMapping("/{userId}/address")
    public ResponseEntity<?> getAddress(@PathVariable("userId") Long userId, @RequestParam(value = "active", required = false) Long active) {
        if(active == null || active != 1L) {
            return ResponseEntity.ok().body(addressRepository.findAllAddressesByUser(userId));
        }

        return ResponseEntity.ok().body(addressRepository.findAddressByUser(userId, true));
    }
}
