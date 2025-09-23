package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.dto.AddQuestionDTO;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.exceptionClasses.QuestionsNotFoundException;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.QuestionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public ResponseEntity<String> addAllQuestions(List<AddQuestionDTO> questions) {

        List<Question> questions1 = new ArrayList<>();

        for (AddQuestionDTO questionDTO : questions)
        {
            Question question = new Question();
            question.setQuestionText(questionDTO.getQuestionText());
            question.setOptions(questionDTO.getOptions());
            question.setCorrectOptionIndex(questionDTO.getCorrectOptionIndex());
            question.setSubject(questionDTO.getSubject());

            questions1.add(question);
        }

        questionDao.saveAllQuestions(questions1);

        return ResponseEntity.ok("All Questions Related to " + questions.get(0).getSubject().name() +" Added Successfully");
    }

//    GET Methods
    @Override
    public ResponseEntity<ResponseStructure<?>> findAllQuestions(String searchParam, String searchValue, int page, int limit) {

        if (searchParam.equalsIgnoreCase("questionId") || searchParam.equalsIgnoreCase("subject"))
        {
            if (searchParam.equalsIgnoreCase("questionId"))
            {
                Question question = questionDao.findQuestionById(searchValue).orElseThrow(() ->
                        new QuestionsNotFoundException("Question Not Found For Given Id"));

                ResponseStructure<Question> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Question Found Successfully For Given Id");
                res.setBody(question);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("subject")) {

                Subject subject = Subject.valueOf(searchValue.toUpperCase());

                Pageable pageable = PageRequest.of(page, limit, Sort.by("questionId").ascending());
                Page<Question> questions = questionDao.findAllQuestionsBySubject(subject, pageable);

                if (questions.isEmpty())
                {
                    throw new QuestionsNotFoundException("Questions Not Found For Given Subject Name");
                }

                ResponseStructure<Page<Question>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Questions Found Successfully For Given Subject Name");
                res.setBody(questions);

                return ResponseEntity.ok(res);
            }
        }

        Pageable pageable = PageRequest.of(page, limit, Sort.by("questionId").ascending());
        Page<Question> allQuestions = questionDao.findAllQuestions(pageable);

        if (allQuestions.isEmpty())
        {
            throw new QuestionsNotFoundException("No Questions Added");
        }

        ResponseStructure<Page<Question>> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("All Questions Found Successfully");
        res.setBody(allQuestions);

        return ResponseEntity.ok(res);
    }
}
