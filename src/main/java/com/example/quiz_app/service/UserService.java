package com.example.quiz_app.service;

import com.example.quiz_app.dto.CandidateDTO;
import com.example.quiz_app.dto.EnrollmentResponseDTO;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseEntity<ResponseStructure<Candidate>> enrollStudent(@Valid CandidateDTO candidateDTO) throws Exception;

    ResponseEntity<ResponseStructure<?>> getAllCandidates(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<ResponseStructure<?>> getAllAnswerSets(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<ResponseStructure<User>> getUserByEmail(String email);

    ResponseEntity<ResponseStructure<QuizInvite>> getQuizInviteByCandidateId(String candidateId);

    ResponseEntity<ResponseStructure<String>> deleteCandidate(String candidateId);

    ResponseEntity<ResponseStructure<?>> findAllQuestions(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<EnrollmentResponseDTO> updateQuizTimeLimit(String candidateId, String timeLimitInMinutes);
}
