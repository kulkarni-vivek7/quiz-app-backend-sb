package com.example.quiz_app.dto;

import com.example.quiz_app.enums.Subject;
import lombok.Data;

import java.util.List;

@Data
public class QuizStartDTO {
    private String candidateId;
    private Subject subject;
    private List<QuestionWithoutAnswerDTO> questions;
}
