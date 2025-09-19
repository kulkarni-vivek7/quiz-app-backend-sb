package com.example.quiz_app.kafka;

import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Question;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Service
public class KafkaConsumerService {

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode rootNode;

    @PostConstruct
    public void initialization() throws Exception {
        // Load questions.json from classpath (src/main/resources)
        ClassPathResource resource = new ClassPathResource("questions.json");
        try (InputStream inputStream = resource.getInputStream()) {
            rootNode = mapper.readTree(inputStream);
        }
    }

    @KafkaListener(topics = "student-answers", groupId = "quiz-group")
    public void consumeAnswers(String message) throws Exception {
        AnswerSet answerSet = mapper.readValue(message, AnswerSet.class);
        List<Question> submittedQuestion = answerSet.getQuestions();

        int total = submittedQuestion.size();

        int correct = 0;

        for (Question q : submittedQuestion) {
            String qid = q.getQuestionId();

            int correctIndex = -1;

            if (rootNode != null) {
                Iterator<String> fields = rootNode.fieldNames();
                search:
                while (fields.hasNext()) {
                    String sub = fields.next();
                    for (JsonNode questionNode : rootNode.get(sub)) {
                        if (questionNode.get("questionId").asText().equals(qid))
                        {
                            correctIndex = questionNode.get("correctOptionIndex").asInt();
                            break search;
                        }
                    }
                }
            }

            String chosenOpt = q.getChosenOptionIndex() >= 0 && q.getChosenOptionIndex() < q.getOptions().size()
                    ? q.getOptions().get(q.getChosenOptionIndex()) : "Invalid Choice";

            String correctOpt = (correctIndex >= 0 && correctIndex < q.getOptions().size())
                    ? q.getOptions().get(correctIndex) : "Unknown";

            if (correctIndex == q.getChosenOptionIndex()) {
                correct++;
            }

            System.out.printf("Q: %s | Chosen: %s | Correct: %s%n",
                    q.getQuestionText(), chosenOpt, correctOpt);
        }

        System.out.printf("Student %s scored %d/%d%n",
                answerSet.getStudentId(), correct, total);
    }
}
