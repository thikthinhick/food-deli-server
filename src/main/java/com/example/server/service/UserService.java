package com.example.server.service;

import com.example.server.entity.User;
import com.example.server.model.CustomUserDetails;
import com.example.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomUserDetails(user);
    }
    public UserDetails loadUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) return new CustomUserDetails(user.get());
        else throw new NullPointerException();
    }
}
