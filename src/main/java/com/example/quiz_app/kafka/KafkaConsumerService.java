package com.example.quiz_app.kafka;

import com.example.quiz_app.dao.AnswerSetDao;
import com.example.quiz_app.dto.AnswerDTO;
import com.example.quiz_app.exceptionClasses.NoAnswerSetFoundException;
import com.example.quiz_app.models.AnswerSet;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KafkaConsumerService {

    private final ObjectMapper mapper = new ObjectMapper();
    private final AnswerSetDao answerSetDao;

    public KafkaConsumerService(AnswerSetDao answerSetDao) {
        this.answerSetDao = answerSetDao;
    }

    @KafkaListener(topics = "candidate-answers", groupId = "quiz-group")
    public void consumeAnswers(String message) throws Exception {
        AnswerSet answerSet = mapper.readValue(message, AnswerSet.class);
        Optional<AnswerSet> answerSetOpt = answerSetDao.findAnswerSetByCandidateId(answerSet.getCandidateId());

        if (answerSetOpt.isEmpty())
        {
            throw new NoAnswerSetFoundException("No stored answer set found for studentId: "+ answerSet.getCandidateId());
        }

        List<AnswerDTO> answers = answerSetOpt.get().getAnswers();

        int total = answers.size();
        int correct = 0;

        for (AnswerDTO answer : answers) {
            if (answer.isCorrect()) {
                correct++;
            }

            System.out.printf("Q: %s | Type: %s | Correct: %s%n",
                    answer.getQuestionId(), answer.getQuestionType(), answer.isCorrect());
        }

        System.out.printf("Candidate %s scored %d/%d%n",
                answerSet.getCandidateId(), correct, total);
    }
}
