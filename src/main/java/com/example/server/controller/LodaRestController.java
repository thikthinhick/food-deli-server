package com.example.server.controller;

import com.example.server.entity.User;
import com.example.server.jwt.JwtTokenProvider;
import com.example.server.model.CustomUserDetails;
import com.example.server.repository.UserRepository;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LodaRestController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/login")
    public String authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
    }
    @GetMapping("/users")
    public List<User> getuser() {
        return userRepository.findAll();
    }
}
@Data
@ToString
class LoginRequest{
    private String username;
    private String password;
}