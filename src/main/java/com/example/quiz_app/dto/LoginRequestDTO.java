package com.example.quiz_app.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String email;

    private String otp;
}
