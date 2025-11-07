package com.example.quiz_app.models;

import com.example.quiz_app.enums.QuestionType;
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
    private QuestionType questionType;
    private Subject subject;
    
    // Fields for MCQ type questions
    private List<String> options;
    private int correctOptionIndex;
    private int chosenOptionIndex;
    
    // Fields for Coding type questions
    private String starterCode;
    private List<TestCase> testCases;
    private String language; // e.g., "java", "python", etc.
    private String candidateCode; // Stores the candidate's submitted code for validation
    
    @Data
    public static class TestCase {
        private String input;
        private String expectedOutput;
        private Boolean isSample; // To identify sample test cases
    }
}