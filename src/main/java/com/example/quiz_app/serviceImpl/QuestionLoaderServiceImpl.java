package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.models.Question;
import com.example.quiz_app.service.QuestionLoaderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@Component
public class QuestionLoaderServiceImpl implements QuestionLoaderService {

//    private static final String QUESTION_FILE_PATH = "../questions.json";

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode rootNode;

    private final Map<String, Integer> questionToCorrectIndex = new HashMap<>();

    @PostConstruct
    public void loadQuestions() throws Exception {
        // Load questions.json from classpath (src/main/resources)
        ClassPathResource resource = new ClassPathResource("questions.json");
        try (InputStream inputStream = resource.getInputStream()) {
            rootNode = mapper.readTree(inputStream);
        }

        for (Iterator<String> subjects = rootNode.fieldNames(); subjects.hasNext();)
        {
            String subject = subjects.next();
            for (JsonNode qNode : rootNode.get(subject)) {
                String qid = qNode.get("questionId").asText();
                int correctIdx = qNode.get("correctOptionIndex").asInt();
                questionToCorrectIndex.put(qid, correctIdx);
            }
        }
    }

    public Integer getCorrectOptionIndex(String questionId)
    {
        return questionToCorrectIndex.get(questionId);
    }

    @Override
    public List<Question> getQuestionsBySubject(String subject, int count) throws Exception {
        List<Question> allQuestions = new ArrayList<>();

        if (rootNode == null || rootNode.get(subject) == null) {
            return allQuestions;
        }

        Iterator<JsonNode> iterator = rootNode.get(subject).elements();
        while (iterator.hasNext()) {
            JsonNode node = iterator.next();
            Question q = mapper.treeToValue(node, Question.class);
            allQuestions.add(q);
        }

        // Shuffle the list to randomize questions order
        Collections.shuffle(allQuestions);

        if(count > allQuestions.size()) {
            count = allQuestions.size();
        }

        return allQuestions.subList(0, count);
    }
}
