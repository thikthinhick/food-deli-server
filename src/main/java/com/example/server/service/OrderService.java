package com.example.server.service;

import com.example.server.entity.*;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.CartRepository;
import com.example.server.repository.OrderFoodRepository;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderFoodRepository orderFoodRepository;
    @Autowired
    private CartRepository cartRepository;
    @Transactional
    public void addCartToOrder(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not find user"));

        List<Cart> carts = user.getCarts();
        Map<Long, List<Cart>> cartGroup =
                carts.stream().collect(Collectors.groupingBy(w -> w.getFood().getRestaurant().getId()));
        Order order = new Order();
        order.setCode(UUID.randomUUID().toString());
        order.setUser(user);
        orderRepository.save(order);
        Set<Long> keys = cartGroup.keySet();
        for(Long key : keys) {
            List<OrderFood> orderFoods = new ArrayList<>();
            for(Cart cart : cartGroup.get(key)) {
                OrderFood orderFood = new OrderFood();
                orderFood.setOrder(order);
                orderFood.setFood(cart.getFood());
                orderFood.setAmount(cart.getAmount());
                orderFoods.add(orderFood);
            }
            orderFoodRepository.saveAll(orderFoods);
        }
        cartRepository.deleteByUserId(userId);
    }
}
