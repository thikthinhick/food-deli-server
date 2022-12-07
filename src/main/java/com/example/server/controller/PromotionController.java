package com.example.server.controller;

import com.example.server.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {
    @Autowired
    private PromotionRepository promotionRepository;
    @GetMapping
    public ResponseEntity<?> getPromotions() {
        return ResponseEntity.ok(promotionRepository.findAll());
    }
}
