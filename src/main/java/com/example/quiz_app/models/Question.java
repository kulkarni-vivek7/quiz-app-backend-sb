package com.example.quiz_app.models;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private String questionId;
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private int chosenOptionIndex;
}