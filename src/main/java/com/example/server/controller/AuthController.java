package com.example.server.controller;

import com.example.server.conmon.AppCommon;
import com.example.server.entity.User;
import com.example.server.exception.ResourceNotFoundException;
import com.example.server.jwt.JwtTokenProvider;
import com.example.server.model.CustomUserDetails;
import com.example.server.repository.OtpRepository;
import com.example.server.repository.TokenRepository;
import com.example.server.repository.UserRepository;
import com.example.server.service.EmailSender;
import com.example.server.service.UserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
class AuthController{

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private OtpRepository otpRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private UserService userService;
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
        User user = userRepository.findByUsername(formRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("khong tim thay ussername"));
        String token = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        FormResponse formResponse = new FormResponse(token, user.getFullName(), user.getUsername(), user.getId(), user.getThumbnail());
        return ResponseEntity.ok(formResponse);
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
        user.setFullName(AppCommon.randomString(10));
        user.setThumbnail("https://cdn.iconscout.com/icon/free/png-256/user-avatar-contact-portfolio-personal-portrait-profile-5093.png");
        user.setRole(User.Role.ROLE_USER);
        userRepository.save(user);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        formRequest.getUsername(),
                        formRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User a = userRepository.findByUsername(formRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("khong tim thay ussername"));
        String token = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        FormResponse formResponse = new FormResponse(token, a.getFullName(), a.getUsername(), a.getId(), a.getThumbnail());
        return ResponseEntity.ok(formResponse);
    }
    @PostMapping("/forgot_password")
    public ResponseEntity<?> forgotPassword(@RequestBody FormRequest formRequest) {
        if (!userRepository.existsByUsername(formRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body("email không tồn tại!");
        }
        String otp = otpRepository.generateOTP(formRequest.getUsername());
        try {
            emailSender.sendMail(formRequest.getUsername(), "RESET PASSWORD", otp);
        } catch (MessagingException e) {
            return ResponseEntity
                    .internalServerError()
                    .body("server bị lỗi!");
        }
        return ResponseEntity.ok("Kiểm tra email để lấy otp!");
    }

    @PostMapping("/check_otp")
    public ResponseEntity<?> resetPassword(@RequestBody FormRequest formRequest) {
        System.out.println(formRequest.getUsername() + formRequest.getOtp());
        try {
            String otp = otpRepository.getState(formRequest.getUsername());
            if(otp.equals(formRequest.getOtp())) {
                otpRepository.delete(formRequest.getUsername());
                String token = tokenRepository.generateOTP(formRequest.getUsername());
                return ResponseEntity.ok(token);
            }
            else return ResponseEntity.badRequest().body("otp không chính xác!");
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("otp hết hạn!");
        }
    }
    @PostMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestBody FormRequest formRequest, HttpServletRequest request) {
        String tokenSend = request.getHeader("reset_password_token");
        if(tokenSend == null) return ResponseEntity.status(401).body("Bạn không thể reset password");
        try{
            String token = tokenRepository.getState(formRequest.getUsername());
            if(token.equals(tokenSend)) {
                tokenRepository.delete(formRequest.getUsername());
                userService.updatePassword(formRequest.getUsername(), encoder.encode(formRequest.getPassword()));
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                formRequest.getUsername(),
                                formRequest.getPassword()
                        )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User a = userRepository.findByUsername(formRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("khong tim thay ussername"));
                String x = tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
                FormResponse formResponse = new FormResponse(x, a.getFullName(), a.getUsername(), a.getId(), a.getThumbnail());
                return ResponseEntity.ok(formResponse);
            }
            else {
                return ResponseEntity.badRequest().body("token không chính xác!");
            }
        }catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("token hết hạn!");
        }
    }
    @PostMapping("/change_password")
    public ResponseEntity<?> responseEntity(@RequestBody FormRequest formRequest) {
        User user = userRepository
                .findByUsername(formRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + formRequest.getUsername()));

        if(encoder.matches(formRequest.getOldPassword(), user.getPassword())) {
            userService.updatePassword(formRequest.getUsername(), encoder.encode(formRequest.getPassword()));
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            formRequest.getUsername(),
                            formRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return ResponseEntity.ok(tokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal()));
        }
        else return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu nhập sai!");
    }
    @Data
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class FormResponse{
        private String accessToken;
        private String fullName;
        private String email;
        private Long userId;
        private String thumbnail;
    }
}
@Data
@ToString

class FormRequest{
    private String username;
    private String password;
    private String oldPassword;
    private String otp;
}
