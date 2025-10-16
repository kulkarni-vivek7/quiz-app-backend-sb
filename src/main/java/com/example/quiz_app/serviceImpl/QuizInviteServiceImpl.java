package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.QuizInviteDao;
import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.QuizStartDTO;
import com.example.quiz_app.exceptionClasses.InvalidInviteTokenException;
import com.example.quiz_app.exceptionClasses.InviteTokenAlreadyUsedException;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.models.QuizInvite;
import com.example.quiz_app.service.QuestionLoaderService;
import com.example.quiz_app.service.QuizInviteService;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuizInviteServiceImpl implements QuizInviteService {

    private final QuizInviteDao quizInviteDao;
    private final QuestionLoaderService questionLoaderService;

    public QuizInviteServiceImpl(QuizInviteDao quizInviteDao, QuestionLoaderService questionLoaderService) {
        this.quizInviteDao = quizInviteDao;
        this.questionLoaderService = questionLoaderService;
    }

    @Override
    public QuizStartDTO startQuiz(String token) throws Exception {
        QuizInvite invite = quizInviteDao.findByToken(token)
                .orElseThrow(() -> new InvalidInviteTokenException("Invalid quiz invite token"));

        if (invite.getExpiresAt() != null && invite.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidInviteTokenException("Quiz invite expired");
        }

        if (Boolean.TRUE.equals(invite.getUsed()))
        {
            throw new InviteTokenAlreadyUsedException("Quiz Invite Already Used... You Can Only Attempt Once");
        }

        List<Question> questions = questionLoaderService.getQuestionsBySubject(invite.getSubject(), 10);
        List<QuestionWithoutAnswerDTO> dtoList = new ArrayList<>();

        for (Question q : questions) {
            QuestionWithoutAnswerDTO dto = new QuestionWithoutAnswerDTO();
            dto.setQuestionId(q.getQuestionId());
            dto.setQuestionText(q.getQuestionText());
            dto.setOptions(q.getOptions());
            dto.setSubject(invite.getSubject());
            dtoList.add(dto);
        }

        QuizStartDTO startDTO = new QuizStartDTO();
        startDTO.setCandidateId(invite.getCandidateId());
        startDTO.setSubject(invite.getSubject());
        startDTO.setQuestions(dtoList);
        return startDTO;
    }
}
