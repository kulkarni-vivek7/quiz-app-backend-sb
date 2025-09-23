package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.AnswerSetDao;
import com.example.quiz_app.dao.QuestionDao;
import com.example.quiz_app.dao.StudentDao;
import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.dto.StudentDTO;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.exceptionClasses.AnswerSetNotFoundException;
import com.example.quiz_app.exceptionClasses.StudentNotFoundException;
import com.example.quiz_app.kafka.KafkaProducerService;
import com.example.quiz_app.models.AnswerSet;
import com.example.quiz_app.models.Question;
import com.example.quiz_app.models.Student;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.MCQService;
import com.example.quiz_app.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;
    private final MCQService mcqService;
    private final KafkaProducerService kafkaProducerService;
//    private final QuestionLoaderServiceImpl questionLoaderServiceImpl;
    private final QuestionDao questionDao;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AnswerSetDao answerSetDao;

    public StudentServiceImpl(StudentDao studentDao, MCQService mcqService, KafkaProducerService kafkaProducerService, QuestionDao questionDao, AnswerSetDao answerSetDao) {
        this.studentDao = studentDao;
        this.mcqService = mcqService;
        this.kafkaProducerService = kafkaProducerService;
        this.questionDao = questionDao;
        this.answerSetDao = answerSetDao;
    }


    @Override
    public ResponseEntity<List<QuestionWithoutAnswerDTO>> enrollStudent(StudentDTO studentDTO) throws Exception {

        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setName(studentDTO.getName());
        student.setAge(studentDTO.getAge());
        student.setEmail(studentDTO.getEmail());
        student.setPhone(studentDTO.getPhone());
        student.setSubject(studentDTO.getSubject());

        studentDao.saveInHashMap(student);

        kafkaProducerService.send("student-enrollments", studentDTO.getId(), mapper.writeValueAsString(studentDTO));

        studentDao.saveInMongoDb(student);

        List<Question> questions = mcqService.getQuestionsForSubject(student.getSubject(), 5);

        List<QuestionWithoutAnswerDTO> result = new ArrayList<>();

        for (Question q : questions) {
            QuestionWithoutAnswerDTO dto = new QuestionWithoutAnswerDTO();
            dto.setQuestionId(q.getQuestionId());
            dto.setQuestionText(q.getQuestionText());
            dto.setOptions(q.getOptions());
            dto.setSubject(student.getSubject());

            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<String> submitAnswers(AnswerSet answerSet) throws Exception  {

        for (Question question : answerSet.getQuestions())
        {
//            Integer correctIdx = questionLoaderServiceImpl.getCorrectOptionIndex(question.getQuestionId());
            Optional<Question> questionOpt = questionDao.findQuestionById(question.getQuestionId());

            questionOpt.ifPresent(value -> question.setCorrectOptionIndex(value.getCorrectOptionIndex()));
        }
        answerSetDao.saveAnswerSet(answerSet);

        kafkaProducerService.send("student-answers", answerSet.getStudentId(), mapper.writeValueAsString(answerSet));

        return ResponseEntity.ok("Answers Submitted!");
    }

//    GET Methods ----------------------------------------------------------------------------------

    @Override
    public ResponseEntity<ResponseStructure<?>> getAllStudents(String searchParam, String searchValue, int page, int limit) {

        if (searchParam.equalsIgnoreCase("id") || searchParam.equalsIgnoreCase("name")
                || searchParam.equalsIgnoreCase("email") || searchParam.equalsIgnoreCase("phone")
                || searchParam.equalsIgnoreCase("subject"))
        {
            if (searchParam.equalsIgnoreCase("id"))
            {
                Student student = studentDao.findStudentById(searchValue).orElseThrow(() ->
                        new StudentNotFoundException("Student Not Found for given Id "+searchValue));

                ResponseStructure<Student> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Student Found Successfully for given ID");
                res.setBody(student);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("name")) {
                Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
                Page<Student> students = studentDao.findStudentsByName(searchValue, pageable);

                if (students.isEmpty())
                {
                    throw new StudentNotFoundException("Students Not Found Given Name");
                }

                ResponseStructure<Page<Student>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Students Found Successfully for given Name");
                res.setBody(students);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("email")) {
                Student student = studentDao.findStudentByEmail(searchValue).orElseThrow(() ->
                        new StudentNotFoundException("Student Not Found for given Email "+searchValue));

                ResponseStructure<Student> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Student Found Successfully for given Email");
                res.setBody(student);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("phone")) {
                Student student = studentDao.findStudentByPhone(searchValue).orElseThrow(() ->
                        new StudentNotFoundException("Student Not Found For Given Phone Number: "+searchValue));

                ResponseStructure<Student> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Student Found Successfully for given Phone Number");
                res.setBody(student);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("subject")) {
                Subject subject = Subject.valueOf(searchValue.toUpperCase());

                Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
                Page<Student> students = studentDao.findStudentsBySubjectName(subject, pageable);

                if (students.isEmpty())
                {
                    throw new StudentNotFoundException("Students Not Found For Given Subject Name");
                }

                ResponseStructure<Page<Student>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Students Found Successfully For Given Subject Name");
                res.setBody(students);

                return ResponseEntity.ok(res);
            }
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
        Page<Student> students = studentDao.findAllStudents(pageable);

        if (students.isEmpty())
        {
            throw new StudentNotFoundException("No Students Registered Yet");
        }

        ResponseStructure<Page<Student>> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("All Registered Students Found Successfully");
        res.setBody(students);

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ResponseStructure<?>> getAllAnswerSets(String searchParam, String searchValue, int page, int limit) {

        if (searchParam.equalsIgnoreCase("studentId"))
        {
            AnswerSet answerSet = answerSetDao.findAnswerSetByStudentId(searchValue).orElseThrow(() ->
                    new AnswerSetNotFoundException("Answer Set Not Found For Given Student Id"));

            ResponseStructure<AnswerSet> res = new ResponseStructure<>();
            res.setStatus(HttpStatus.OK.value());
            res.setMessage("Answer Set Found For Given Student Id");
            res.setBody(answerSet);

            return ResponseEntity.ok(res);
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by("studentId").ascending());
        Page<AnswerSet> answerSets = answerSetDao.findAllAnswerSets(pageable);

        if (answerSets.isEmpty())
        {
            throw new AnswerSetNotFoundException("No Answers Sets Found");
        }

        ResponseStructure<Page<AnswerSet>> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("All AnswerSets Found Successfully");
        res.setBody(answerSets);

        return ResponseEntity.ok(res);
    }
}