package com.example.quiz_app.service;

import com.example.quiz_app.dto.LoginRequestDTO;
import com.example.quiz_app.dto.UserRegistrationDTO;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    ResponseEntity<ResponseStructure<User>> userRegistration(UserRegistrationDTO userRegistrationDTO);

    ResponseEntity<ResponseStructure<String>> sendOtpEmail(String email);

    ResponseEntity<ResponseStructure<String>> loginWithOtp(LoginRequestDTO loginRequestDTO);
}
