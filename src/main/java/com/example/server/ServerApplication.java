package com.example.server;

import com.example.server.entity.Category;
import com.example.server.entity.Food;
import com.example.server.entity.User;
import com.example.server.repository.CategoryRepository;
import com.example.server.repository.FoodRepository;
import com.example.server.repository.RestaurantRepository;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    FoodRepository foodRepository;
    @Override
    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setUsername("trumle2k1@gmail.com");
//        user.setPassword(passwordEncoder.encode("chuong2001"));
//        Category category1 = new Category();
//        category1.setName("Phở");
//        Category category2 = new Category();
//        category2.setName("Cơm tấm");
//        Food food1 = new Food();
//        food1.setName("Phở 10 Lý Quốc Sư");
//        food1.setDescription("Phở được nấu chuẩn vị của Hà Nội");
//        food1.setPrice(100000L);
//        food1.setOldPrice(120000L);
//        Category category = categoryRepository.save(category1);
//        food1.getCategory().add(category);
//        foodRepository.save(food1);
//        userRepository.save(user);
//        System.out.println(userRepository.findAll());
    }
}
