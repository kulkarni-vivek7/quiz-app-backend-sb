package com.example.quiz_app.service;

import com.example.quiz_app.dto.CandidateDTO;
import com.example.quiz_app.dto.EnrollmentResponseDTO;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    ResponseEntity<EnrollmentResponseDTO> enrollStudent(@Valid CandidateDTO candidateDTO) throws Exception;

    ResponseEntity<ResponseStructure<?>> getAllCandidates(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<ResponseStructure<?>> getAllAnswerSets(String searchParam, String searchValue, int page, int limit);

    ResponseEntity<ResponseStructure<User>> getUserByEmail(String email);

    ResponseEntity<ResponseStructure<QuizInvite>> getQuizInviteByCandidateId(String candidateId);

    ResponseEntity<ResponseStructure<String>> deleteCandidate(String candidateId);

    ResponseEntity<ResponseStructure<Page<Candidate>>> getCandidatesByQuizInviteUsedTrue(int page, int limit);
}
