package com.example.quiz_app.models;

import com.example.quiz_app.enums.Subject;
import lombok.Data;

@Data
public class Student {
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;
    private Subject subject;
}
