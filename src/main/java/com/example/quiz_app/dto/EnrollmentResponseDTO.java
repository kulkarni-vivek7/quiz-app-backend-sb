package com.example.quiz_app.dto;

import com.example.quiz_app.enums.Subject;
import lombok.Data;

@Data
public class EnrollmentResponseDTO {
    private String candidateId;
    private Subject subject;
    private String inviteLink;
}