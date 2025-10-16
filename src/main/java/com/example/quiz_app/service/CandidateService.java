package com.example.quiz_app.service;

import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.response.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CandidateService {

    ResponseEntity<ResponseStructure<AnswerSet>> submitAnswers(String token, @Valid AnswerSet answerSet) throws Exception;

    ResponseEntity<ResponseStructure<Candidate>> getCandidateByToken(String token);
}
