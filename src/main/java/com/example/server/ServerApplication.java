package com.example.server;

import com.example.server.entity.Address;
import com.example.server.entity.Food;
import com.example.server.entity.Restaurant;
import com.example.server.entity.User;
import com.example.server.repository.*;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setUsername("trumle2k1@gmail.com");
        user.setPassword(passwordEncoder.encode("chuong2001"));
        userRepository.save(user);
        Address address = new Address();
        address.setActive(true);
        address.setDetail("Số 2, ngõ 27");
        address.setAwards("Thanh Xuan");
        address.setDistrict("Đống Đa");
        address.setProvince("Hà Nội");
        address.setLatitude(1000.23);
        address.setLongitude(550.23);
        address.setUser(user);
        addressRepository.save(address);

        Food food = new Food();
        food.setPrice(10000L);
        food.setOldPrice(20000L);
        food.setName("Bán mì trứng xúc xích");
        food.setDescription("Sản phẩm 100% làm từ thịt sạch");

        Restaurant restaurant = new Restaurant();
        restaurant.setBackgroundImage("https://google.com.vn");
        restaurant.setName("Bánh mì pewpew");
        restaurant.setDescription("Nhà hàng đạt chuẩn 5 sao");
        restaurantRepository.save(restaurant);
        food.setRestaurant(restaurant);
        foodRepository.save(food);
        userService.addFoodToCart(user.getId(), food.getId());
    }
}
