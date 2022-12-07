package com.example.server.service;

import com.example.server.entity.Cart;
import com.example.server.entity.Food;
import com.example.server.entity.User;
import com.example.server.model.CustomUserDetails;
import com.example.server.repository.FoodRepository;
import com.example.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    public UserService(UserRepository userRepository, FoodRepository foodRepository) {
        this.userRepository = userRepository;
        this.foodRepository = foodRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new CustomUserDetails(user);
    }

    public void updatePassword(String username, String password) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + username));
        user.setPassword(password);
        userRepository.save(user);
    }

    @Transactional
    public void addFoodToCart(Long userId, Long foodId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NullPointerException("Not find user"));
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new NullPointerException("Not find food"));
        for(Cart cart : user.getCarts()) {
            if(Objects.equals(cart.getFood().getId(), food.getId())) {
                cart.setAmount(cart.getAmount() + 1);
                userRepository.save(user);
                return;
            }
        }
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setFood(food);
        cart.setAmount(1);
        user.getCarts().add(cart);
        userRepository.save(user);
    }

}
