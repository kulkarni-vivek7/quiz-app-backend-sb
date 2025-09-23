package com.example.quiz_app.models;

import com.example.quiz_app.enums.Subject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Question {
    @Id
    private String questionId;

    private String questionText;

    private List<String> options;

    private int correctOptionIndex;

    private int chosenOptionIndex;

    private Subject subject;
}