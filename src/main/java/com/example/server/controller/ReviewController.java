package com.example.server.controller;

import com.example.server.entity.Order;
import com.example.server.entity.Review;
import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.repository.OrderRepository;
import com.example.server.repository.ReviewRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.OrderService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest review) {
        if (!userRepository.existsById(review.getUserId()))
            throw new ResourceNotFoundException("Không tìm thấy user");
        if (!orderRepository.existsById(review.getOrderId()))
            throw new ResourceNotFoundException("Không tìm thấy order");
        Order order = orderRepository.findById(review.getOrderId()).get();
        User user = userRepository.findById(review.getUserId()).get();
        if(order.getStatuses().get(order.getStatuses().size() - 1).getStatus().getId() != 6)
            return ResponseEntity.badRequest().body("Không được phép đánh giá đơn hàng!");
        if(reviewRepository.existsByUserAndOrder(review.getUserId(), review.getOrderId()) != 0)
            throw new ResourceNotFoundException("Đơn hàng đã được đánh giá");
        Review re = new Review();
        re.setOrder(order);
        re.setRestaurant(order.getOrderFoods().get(0).getFood().getRestaurant());
        re.setUser(user);
        re.setTime(new Timestamp(System.currentTimeMillis()));
        re.setRate(review.getRate());
        re.setMessage(review.getMessage());
        reviewRepository.save(re);
        orderService.changeStatusOrder(review.getOrderId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Tao review thanh cong");
    }
}

@Data

class ReviewRequest {
    private Long userId;
    private Long orderId;
    private int rate;
    private String message;
}
