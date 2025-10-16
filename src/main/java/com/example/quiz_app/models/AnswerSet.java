package com.example.quiz_app.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class AnswerSet {
    @Id
    private String candidateId;
    private List<Question> questions;
}
