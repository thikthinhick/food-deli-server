package com.example.server.controller;

import com.example.server.entity.*;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.*;
import com.example.server.service.OrderService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RequestMapping("/api/orders")
@RestController
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderFoodRepository orderFoodRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private OrderStatusRepository orderStatusRepository;
    @PostMapping
    public void createOrders( @RequestBody OrderRequest orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Not find user"));
        for(ItemOrderRequest item : orderRequest.getOrders()) {
            Order order = new Order();
            order.setCode(UUID.randomUUID().toString());
            order.setUser(user);
            order.setPrice(item.getPrice());
            order.setAddress(orderRequest.getAddress());
            orderRepository.save(order);
            Status status = statusRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("error server"));
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setOrder(order);
            orderStatus.setStatus(status);
            orderStatus.setTime(new Timestamp(System.currentTimeMillis()));
            orderStatusRepository.save(orderStatus);
            List<OrderFood> orderFoods = new ArrayList<>();
            List<Long> ids = item.getFoods().stream().map(FoodRequest::getFoodId).collect(Collectors.toList());
            List<Food> foods = foodRepository.findFoodByList(ids);
            for(Food food : foods) {
                OrderFood orderFood = new OrderFood();
                orderFood.setOrder(order);
                orderFood.setFood(food);
                int amount = item.getFoods().stream().filter(element -> element.getFoodId() == food.getId()).map(FoodRequest::getAmount).findFirst().get();
                orderFood.setAmount(amount);
                orderFoods.add(orderFood);
            }
            orderFoodRepository.saveAll(orderFoods);
        }
    }
    @GetMapping
    public ResponseEntity<?> getOrder(@RequestParam(value = "id", required = false) Long id) {
        if(id == null) {
            List<OrderResponse> orderResponses = new ArrayList<>();
            orderRepository.findAll().forEach(element -> {
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setCode(element.getCode());
                orderResponse.setId(element.getId());
                orderResponse.setPrice(element.getPrice());
                orderResponse.setStatus(element.getStatuses().get(element.getStatuses().size() - 1).getStatus());
                AtomicInteger total = new AtomicInteger();
                element.getOrderFoods().forEach(item -> {
                    total.set(total.get() + item.getAmount());
                });
                orderResponse.setFoods(element.getOrderFoods());
                orderResponse.setTotal(total.get());
                orderResponse.setRestaurantName(element.getOrderFoods().get(0).getFood().getRestaurant().getName());
                orderResponse.setImages(element.getOrderFoods().get(0).getFood().getImages());
                orderResponses.add(orderResponse);
            });
            return ResponseEntity.ok().body(orderResponses);
        }
        Order element = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not find orrder"));
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCode(element.getCode());
        orderResponse.setId(element.getId());
        orderResponse.setPrice(element.getPrice());
        orderResponse.setStatus(element.getStatuses().get(element.getStatuses().size() - 1).getStatus());
        orderResponse.setRestaurantName(element.getOrderFoods().get(0).getFood().getRestaurant().getName());
        orderResponse.setFoods(element.getOrderFoods()
                .stream().peek(x -> x.getFood().setRestaurant(null)).collect(Collectors.toList()));
        orderResponse.setAddress(element.getAddress());
        return ResponseEntity.ok().body(orderResponse);
    }
    @GetMapping("/{orderId}/statuses")
    public ResponseEntity<?> getStatuses(@PathVariable("orderId") Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Not find orrder"));
        return ResponseEntity.ok().body(order.getStatuses());
    }
    @PutMapping
    public ResponseEntity<?> changeStatusOrder(@RequestParam(value = "id", required = false) Long id) {
        orderService.changeStatusOrder(id);
        return ResponseEntity.ok().body("cap nhat thanh cong!");
    }
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class OrderResponse{
        private String code;
        private String restaurantName;
        private int total;
        private Long id;
        private String state;
        private Set<Image> images;
        private Long price;
        private List<OrderFood> foods;
        private String address;
        private Status status;
    }
    @Data
    private static class ItemOrderRequest{
        private Long price;
        private List<FoodRequest> foods;
    }
    @Data
    private static class OrderRequest{
        private List<ItemOrderRequest> orders;
        private String address;
        private String time;
        private Long userId;
    }
    @Data
    private static class FoodRequest{
        private Long foodId;
        private int amount;
    }
}


