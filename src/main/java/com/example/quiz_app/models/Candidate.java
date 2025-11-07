package com.example.quiz_app.models;

import com.example.quiz_app.enums.Subject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Candidate {
    @Id
    private String id;
    private String name;
    private int age;
    private String email;
    private String phone;
    private List<Subject> subject;
}
