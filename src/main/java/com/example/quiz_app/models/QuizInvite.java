package com.example.quiz_app.models;

import com.example.quiz_app.enums.Subject;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document
public class QuizInvite {
    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    private String candidateId;

    private Subject subject;

    private Instant createdAt;

    private Instant expiresAt;

    private Boolean used;

    private Instant usedAt;
}
