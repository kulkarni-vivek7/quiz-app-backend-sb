package com.example.quiz_app.controller;

import com.example.quiz_app.dto.CandidateDTO;
import com.example.quiz_app.dto.EnrollmentResponseDTO;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hr")
@CrossOrigin("http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/enrollCandidate")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<Candidate>> enrollCandidate(
            @Valid
            @RequestBody CandidateDTO candidateDTO) throws Exception
    {
        return userService.enrollStudent(candidateDTO);
    }

    //    GET Mappings-----------------------------------------------------------------------

    @GetMapping("/byEmail")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<User>> getUserByEmail(@RequestParam String email)
    {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/getCandidates")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<?>> getAllCandidates(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return userService.getAllCandidates(searchParam, searchValue, page, limit);
    }

    @GetMapping("/getAnswerSets")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<?>> getAllAnswerSets(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return userService.getAllAnswerSets(searchParam, searchValue, page, limit);
    }

    @GetMapping("/getQuizInviteByCandidateId")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<QuizInvite>> getQuizInviteByCandidateId(
            @RequestParam String candidateId
    )
    {
        return userService.getQuizInviteByCandidateId(candidateId);
    }

    @GetMapping("/getAllQuestions")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<?>> getAllQuestions(
            @RequestParam String searchParam,
            @RequestParam String searchValue,
            @RequestParam int page,
            @RequestParam int limit
    ) {
        return userService.findAllQuestions(searchParam, searchValue, page, limit);
    }

//    PUT Methods----------------------------------------------------------------------------

    @PutMapping("/updateQuizTimeLimit")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<EnrollmentResponseDTO> updateQuizTimeLimit(
            @RequestParam String candidateId,
            @RequestParam String timeLimitInMinutes
    )
    {
        return userService.updateQuizTimeLimit(candidateId, timeLimitInMinutes);
    }

//    DELETE Methods------------------------------------------------------------------------
    @DeleteMapping("/deleteCandidate")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<String>> deleteCandidate(
            @RequestParam String candidateId
    )
    {
        return userService.deleteCandidate(candidateId);
    }
}
