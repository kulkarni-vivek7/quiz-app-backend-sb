package com.example.quiz_app.controller;

import com.example.quiz_app.dto.LoginRequestDTO;
import com.example.quiz_app.dto.UserRegistrationDTO;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseStructure<User>> register(
            @RequestBody UserRegistrationDTO userRegistrationDTO
            )
    {
        return loginService.userRegistration(userRegistrationDTO);
    }

    @GetMapping("/send-otp-email")
    public ResponseEntity<ResponseStructure<String>> sendOtpEmail(
            @RequestParam String email
    )
    {
        return loginService.sendOtpEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseStructure<String>> login(
            @RequestBody LoginRequestDTO loginRequestDTO
            )
    {
        return loginService.loginWithOtp(loginRequestDTO);
    }
}
