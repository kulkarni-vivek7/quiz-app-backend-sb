package com.example.quiz_app.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerSetDTO {
    private String studentId;
    private List<QuestionDTO> questions;
}
