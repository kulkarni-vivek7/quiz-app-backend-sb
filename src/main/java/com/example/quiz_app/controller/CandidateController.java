package com.example.quiz_app.controller;

import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.CandidateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin("http://localhost:5173")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/answer")
    public ResponseEntity<ResponseStructure<AnswerSet>> submitAnswers(@RequestParam String token, @RequestBody AnswerSet answerSet)
        throws Exception {

        return candidateService.submitAnswers(token, answerSet);
    }

    @GetMapping("/getCandidateByToken")
    public ResponseEntity<ResponseStructure<Candidate>> getCandidateByToken(
            @RequestParam String token
    )
    {
        return candidateService.getCandidateByToken(token);
    }
}
