package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.StudentDao;
import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.StudentDTO;
import com.example.quiz_app.kafka.KafkaProducerService;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.models.Student;
import com.example.quiz_app.service.MCQService;
import com.example.quiz_app.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final MCQService mcqService;
    private final KafkaProducerService kafkaProducerService;
    private final QuestionLoaderServiceImpl questionLoaderServiceImpl;
    private final ObjectMapper mapper = new ObjectMapper();

    public StudentServiceImpl(StudentDao studentDao, MCQService mcqService, KafkaProducerService kafkaProducerService, QuestionLoaderServiceImpl questionLoaderServiceImpl) {
        this.studentDao = studentDao;
        this.mcqService = mcqService;
        this.kafkaProducerService = kafkaProducerService;
        this.questionLoaderServiceImpl = questionLoaderServiceImpl;
    }


    @Override
    public ResponseEntity<List<QuestionWithoutAnswerDTO>> enrollStudent(StudentDTO studentDTO) throws Exception {

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setAge(student.getAge());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setSubject(studentDTO.getSubject());

        studentDao.save(student);

        kafkaProducerService.send("student-enrollments", studentDTO.getId(), mapper.writeValueAsString(studentDTO));

        List<Question> questions = mcqService.getQuestionsForSubject(studentDTO.getSubject().name(), 5);

        List<QuestionWithoutAnswerDTO> result = new ArrayList<>();

        for (Question q : questions) {
            QuestionWithoutAnswerDTO dto = new QuestionWithoutAnswerDTO();
            dto.setQuestionId(q.getQuestionId());
            dto.setQuestionText(q.getQuestionText());
            dto.setOptions(q.getOptions());

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<String> submitAnswers(AnswerSet answerSet) throws Exception  {

        for (Question question : answerSet.getQuestions())
        {
            Integer correctIdx = questionLoaderServiceImpl.getCorrectOptionIndex(question.getQuestionId());

            if (correctIdx != null)
            {
                question.setCorrectOptionIndex(correctIdx);
            }
        }

        kafkaProducerService.send("student-answers", answerSet.getStudentId(), mapper.writeValueAsString(answerSet));
        return ResponseEntity.ok("Answers Submitted!");
    }
}
