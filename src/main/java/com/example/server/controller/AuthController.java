package com.example.server.controller;

import com.example.server.entity.User;
import com.example.server.jwt.JwtTokenProvider;
import com.example.server.model.CustomUserDetails;
import com.example.server.repository.TokenRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.EmailSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RestController
@RequestMapping("/api")
class AuthController{

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailSender emailSender;
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody FormRequest formRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        formRequest.getUsername(),
                        formRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal()));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody FormRequest formRequest) {
        if (userRepository.existsByUsername(formRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("email tạo tài khoản đã tồn tại!");
        }
        User user = new User();
        user.setUsername(formRequest.getUsername());
        user.setPassword(encoder.encode(formRequest.getPassword()));
        user.setRole(User.Role.ROLE_USER);
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        formRequest.getUsername(),
                        formRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal()));
    }
    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody FormRequest formRequest) {
        if (!userRepository.existsByUsername(formRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("email không tồn tại!");
        }
        String otp = tokenRepository.generateOTP(formRequest.getUsername());
        try {
            emailSender.sendMail(formRequest.getUsername(), "RESET PASSWORD", otp);
        } catch (MessagingException e) {
            return ResponseEntity
                    .internalServerError()
                    .body("server bị lỗi!");
        }
        return ResponseEntity.ok("Kiểm tra email để lấy otp!");
    }
}
@Data
@ToString

class FormRequest{
    private String username;
    private String password;
}