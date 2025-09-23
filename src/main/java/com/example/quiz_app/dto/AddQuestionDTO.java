package com.example.quiz_app.dto;

import com.example.quiz_app.enums.Subject;
import lombok.Data;

import java.util.List;

@Data
public class AddQuestionDTO {
    private String questionText;
    private List<String> options;
    private int correctOptionIndex;
    private Subject subject;
}
