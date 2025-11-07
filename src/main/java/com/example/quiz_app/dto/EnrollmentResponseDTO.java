package com.example.quiz_app.dto;

import com.example.quiz_app.enums.Subject;
import lombok.Data;

import java.util.List;

@Data
public class EnrollmentResponseDTO {
    private String candidateId;
    private List<Subject> subject;
    private String inviteLink;
    private String quizTimeLimit;
}