package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.AnswerSetDao;
import com.example.quiz_app.dao.CandidateDao;
import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.exceptionClasses.CandidateNotFoundException;
import com.example.quiz_app.exceptionClasses.InvalidInviteTokenException;
import com.example.quiz_app.kafka.KafkaProducerService;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.quiz_app.dao.QuizInviteDao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class CandidateServiceImpl implements CandidateService {

    private final KafkaProducerService kafkaProducerService;
    private final QuestionDao questionDao;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AnswerSetDao answerSetDao;
    private final QuizInviteDao quizInviteDao;
    private final CandidateDao candidateDao;

    public CandidateServiceImpl(KafkaProducerService kafkaProducerService, QuestionDao questionDao, AnswerSetDao answerSetDao, QuizInviteDao quizInviteDao, CandidateDao candidateDao) {
        this.kafkaProducerService = kafkaProducerService;
        this.questionDao = questionDao;
        this.answerSetDao = answerSetDao;
        this.quizInviteDao = quizInviteDao;
        this.candidateDao = candidateDao;
    }

    @Override
    public ResponseEntity<ResponseStructure<AnswerSet>> submitAnswers(String token, AnswerSet answerSet) throws Exception  {

        QuizInvite invite = quizInviteDao.findByToken(token)
                .orElseThrow(() -> new InvalidInviteTokenException("Invalid quiz invite token"));

        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidInviteTokenException("Quiz invite expired");
        }

        if (answerSet.getCandidateId() == null || answerSet.getCandidateId().isBlank()) {
            answerSet.setCandidateId(invite.getCandidateId());
        } else if (!invite.getCandidateId().equals(answerSet.getCandidateId())) {
            throw new InvalidInviteTokenException("AnswerSet candidate mismatch for token");
        }

        for (Question question : answerSet.getQuestions())
        {
            Optional<Question> questionOpt = questionDao.findQuestionById(question.getQuestionId());

            questionOpt.ifPresent(value -> question.setCorrectOptionIndex(value.getCorrectOptionIndex()));
        }
        answerSetDao.saveAnswerSet(answerSet);

        kafkaProducerService.send("candidate-answers", answerSet.getCandidateId(), mapper.writeValueAsString(answerSet));

        invite.setUsed(Boolean.TRUE);
        invite.setUsedAt(Instant.now());
        quizInviteDao.save(invite);

        ResponseStructure<AnswerSet> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.CREATED.value());
        res.setMessage("AnswerSet Submitted Successfully");
        res.setBody(answerSet);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseStructure<Candidate>> getCandidateByToken(String token) {
        QuizInvite invite = quizInviteDao.findByToken(token).orElseThrow(() ->
                new CandidateNotFoundException("Invalid Quiz Invite Token"));

        Candidate candidate = candidateDao.findCandidateById(invite.getCandidateId()).orElseThrow(() ->
                new CandidateNotFoundException("Candidate Not Found for given Id"));

        ResponseStructure<Candidate> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Candidate Found Successfully for given Token");
        res.setBody(candidate);

        return ResponseEntity.ok(res);
    }
}