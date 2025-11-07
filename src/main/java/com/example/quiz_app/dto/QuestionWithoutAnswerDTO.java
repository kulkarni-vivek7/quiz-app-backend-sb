package com.example.quiz_app.dto;

import com.example.quiz_app.enums.QuestionType;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Question;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class QuestionWithoutAnswerDTO {
    private String questionId;
    private String questionText;
    private QuestionType questionType;
    private Subject subject;
    
    // For MCQ questions
    private List<String> options;
    
    // For Coding questions
    private String starterCode;
    private List<Question.TestCase> sampleTestCases; // Only include sample test cases
    private String language;
    
    public static QuestionWithoutAnswerDTO fromQuestion(Question question) {
        QuestionWithoutAnswerDTO dto = new QuestionWithoutAnswerDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setQuestionText(question.getQuestionText());
        dto.setQuestionType(question.getQuestionType());
        dto.setSubject(question.getSubject());
        
        if (question.getQuestionType() == QuestionType.MCQ) {
            dto.setOptions(question.getOptions());
        } else if (question.getQuestionType() == QuestionType.CODING) {
            dto.setStarterCode(question.getStarterCode());
            dto.setLanguage(question.getLanguage());
            // Filter only sample test cases to send to the client
            List<Question.TestCase> sampleCases = question.getTestCases().stream()
                    .filter(Question.TestCase::getIsSample)
                    .collect(Collectors.toList());
            dto.setSampleTestCases(sampleCases);
        }
        
        return dto;
    }
}