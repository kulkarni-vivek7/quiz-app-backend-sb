package com.example.quiz_app.controller;

import com.example.quiz_app.dto.CandidateDTO;
import com.example.quiz_app.dto.EnrollmentResponseDTO;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.models.User;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<EnrollmentResponseDTO> enrollStudent(
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

    @GetMapping("/getCandidatesByQuizInviteUsedTrue")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<ResponseStructure<Page<Candidate>>> getCandidatesByQuizInviteUsedTrue(
            @RequestParam int page,
            @RequestParam int limit
    )
    {
        return userService.getCandidatesByQuizInviteUsedTrue(page, limit);
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

//    PUT Methods----------------------------------------------------------------------------

//    @PutMapping("/updateCandidate")
//    @PreAuthorize("hasRole('HR')")
//    public ResponseEntity<ResponseStructure<Candidate>> updateCandidate(
//            @RequestParam String candidateEmail,
//            @RequestBody Candidate candidate
//    )
//    {
//        return userService.updateCandidate(candidateEmail, candidate);
//    }

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
