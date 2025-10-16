package com.example.quiz_app.kafka;

import com.example.quiz_app.dao.AnswerSetDao;
import com.example.quiz_app.exceptionClasses.NoAnswerSetFoundException;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Question;
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

        List<Question> questions = answerSetOpt.get().getQuestions();

        int total = questions.size();

        int correct = 0;

        for (Question q : questions) {

            int correctIndex = q.getCorrectOptionIndex();
            int chosenIndex = q.getChosenOptionIndex();
            String chosenOpt = (chosenIndex >= 0 && chosenIndex < q.getOptions().size()) ? q.getOptions().get(chosenIndex) : "Invalid Choice";
            String correctOpt = (correctIndex >= 0 && correctIndex < q.getOptions().size()) ? q.getOptions().get(correctIndex) : "Unknown";

            if (correctIndex == chosenIndex) {
                correct++;
            }

            System.out.printf("Q: %s | Chosen: %s | Correct: %s%n",
                    q.getQuestionText(), chosenOpt, correctOpt);
        }

        System.out.printf("Candidate %s scored %d/%d%n",
                answerSet.getCandidateId(), correct, total);
    }
}
