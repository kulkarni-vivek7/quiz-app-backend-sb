package com.example.quiz_app.models;

import com.example.quiz_app.enums.Status;
import com.example.quiz_app.enums.UserRole;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class LoginDetails {

    @Id
    private String id;

    private String email;

    private Long phone;

    private String otp;

    private UserRole role;

    private Status status;

    private LocalDateTime timeStamp;

    private Boolean isLoggedIn;
}
