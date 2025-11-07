package com.example.quiz_app.dto;

import com.example.quiz_app.enums.QuestionType;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import lombok.Data;

import java.util.List;

@Data
public class AddQuestionDTO {
    private String questionText;
    private QuestionType questionType;
    private Subject subject;
    
    // Fields for MCQ type questions
    private List<String> options;
    private int correctOptionIndex;
    
    // Fields for Coding type questions
    private String starterCode;
    private List<Question.TestCase> testCases;
    private String language; // e.g., "java", "python", etc.
    
    public Question toQuestion() {
        Question question = new Question();
        question.setQuestionText(this.questionText);
        question.setQuestionType(this.questionType);
        question.setSubject(this.subject);
        
        if (this.questionType == QuestionType.MCQ) {
            question.setOptions(this.options);
            question.setCorrectOptionIndex(this.correctOptionIndex);
        } else if (this.questionType == QuestionType.CODING) {
            question.setStarterCode(this.starterCode);
            question.setTestCases(this.testCases);
            question.setLanguage(this.language.toLowerCase());
        }
        
        return question;
    }
}
