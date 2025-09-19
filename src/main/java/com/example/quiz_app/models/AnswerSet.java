package com.example.quiz_app.models;

import lombok.Data;

import java.util.List;

@Data
public class AnswerSet {
    private String studentId;
    private List<Question> questions;
}
