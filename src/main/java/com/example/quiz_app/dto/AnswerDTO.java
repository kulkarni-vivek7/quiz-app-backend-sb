package com.example.quiz_app.dto;

import com.example.quiz_app.enums.QuestionType;
import lombok.Data;

import java.util.List;

@Data
public class AnswerDTO {
    private String questionId;
    private String questionText; // optional, for reference
    private QuestionType questionType;

    // For MCQ questions
    private Integer chosenOptionIndex;

    private List<String> options; // optional, for reference

    private Integer correctOptionIndex; // optional, for reference

    // For Coding questions
    private String candidateCode;
    private String language;
//    optional, for reference
    private int passedTestCases;
    private int totalTestCases;

    // For scoring
    private boolean isCorrect;
}
