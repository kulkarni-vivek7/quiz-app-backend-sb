package com.example.quiz_app.models;

import com.example.quiz_app.dto.AnswerDTO;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class AnswerSet {
    @Id
    private String candidateId;
    private List<AnswerDTO> answers;
    private String timeTaken;
    private int totalQuestions;
    private int correctAnswers;
    private boolean isCompleted;
}
