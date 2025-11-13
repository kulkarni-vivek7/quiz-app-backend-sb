package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.dao.*;
import com.example.quiz_app.dto.CandidateDTO;
import com.example.quiz_app.dto.EnrollmentResponseDTO;
import com.example.quiz_app.dto.QuestionWithoutAnswerDTO;
import com.example.quiz_app.enums.QuestionType;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.exceptionClasses.*;
import com.example.quiz_app.inviteMailSender.InviteEmailSenderService;
import com.example.quiz_app.kafka.KafkaProducerService;
import com.example.quiz_app.models.*;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private final CandidateDao candidateDao;
    private final KafkaProducerService kafkaProducerService;
    private final InviteEmailSenderService inviteEmailSenderService;
    private final QuizInviteDao quizInviteDao;
    private final ObjectMapper mapper = new ObjectMapper();
    private final AnswerSetDao answerSetDao;
    private final UserDao userDao;
    private final LoginDao loginDao;
    private final QuestionDao questionDao;

    public UserServiceImpl(CandidateDao candidateDao, KafkaProducerService kafkaProducerService, InviteEmailSenderService inviteEmailSenderService, QuizInviteDao quizInviteDao, AnswerSetDao answerSetDao, UserDao userDao, LoginDao loginDao, QuestionDao questionDao) {
        this.candidateDao = candidateDao;
        this.kafkaProducerService = kafkaProducerService;
        this.inviteEmailSenderService = inviteEmailSenderService;
        this.quizInviteDao = quizInviteDao;
        this.answerSetDao = answerSetDao;
        this.userDao = userDao;
        this.loginDao = loginDao;
        this.questionDao = questionDao;
    }

    @Value("${app.frontend.quiz-url}")
    private String quizFrontendUrl;

    @Override
    public ResponseEntity<ResponseStructure<Candidate>> enrollStudent(CandidateDTO candidateDTO) throws Exception {

        Optional<LoginDetails> loginDetailsOptional1 = loginDao.findByEmail(candidateDTO.getEmail());

        if (loginDetailsOptional1.isPresent())
        {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Optional<LoginDetails> loginDetailsOptional2 = loginDao.findByPhone(candidateDTO.getPhone());

        if (loginDetailsOptional2.isPresent())
        {
            throw new PhoneNumberAlreadyExistsException("Phone number already in use");
        }

        Optional<Candidate> candidateOptional1 = candidateDao.findCandidateByEmail(candidateDTO.getEmail());

        if (candidateOptional1.isPresent())
        {
            throw new EmailAlreadyExistsException("Email already in use");
        }

        Optional<Candidate> candidateOptional2 = candidateDao.findCandidateByPhone(candidateDTO.getPhone());

        if (candidateOptional2.isPresent())
        {
            throw new PhoneNumberAlreadyExistsException("Phone number already in use");
        }

        Candidate candidate = new Candidate();
        candidate.setName(candidateDTO.getName());
        candidate.setAge(candidateDTO.getAge());
        candidate.setEmail(candidateDTO.getEmail());
        candidate.setPhone(candidateDTO.getPhone());
        candidate.setSubject(candidateDTO.getSubject());

        // Persist and get generated ID
        Candidate savedCandidate = candidateDao.saveInMongoDb(candidate);
        candidateDao.saveInHashMap(savedCandidate);

        kafkaProducerService.send("candidate-enrollments", savedCandidate.getId(), mapper.writeValueAsString(savedCandidate));

        // Create invite token
        String token = UUID.randomUUID().toString();
        Instant now = Instant.now();
        QuizInvite invite = new QuizInvite();
        invite.setCandidateId(savedCandidate.getId());
        invite.setSubject(savedCandidate.getSubject());
        invite.setToken(token);
        invite.setCreatedAt(now);
        invite.setExpiresAt(now.plus(3, ChronoUnit.HOURS));
        invite.setUsed(Boolean.FALSE);
        quizInviteDao.save(invite);

        ResponseStructure<Candidate> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.CREATED.value());
        res.setMessage("Candidate Enrolled Successfully");
        res.setBody(savedCandidate);

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseStructure<?>> getAllCandidates(String searchParam, String searchValue, int page, int limit) {

        if (searchParam.equalsIgnoreCase("id") || searchParam.equalsIgnoreCase("name")
                || searchParam.equalsIgnoreCase("email") || searchParam.equalsIgnoreCase("phone")
                || searchParam.equalsIgnoreCase("subject"))
        {
            if (searchParam.equalsIgnoreCase("id"))
            {
                Candidate candidate = candidateDao.findCandidateById(searchValue).orElseThrow(() ->
                        new CandidateNotFoundException("Candidate Not Found for given Id "+searchValue));

                ResponseStructure<Candidate> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Candidate Found Successfully for given ID");
                res.setBody(candidate);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("name")) {
                Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
                Page<Candidate> students = candidateDao.findCandidatesByName(searchValue, pageable);

                if (students.isEmpty())
                {
                    throw new CandidateNotFoundException("Students Not Found Given Name");
                }

                ResponseStructure<Page<Candidate>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Students Found Successfully for given Name");
                res.setBody(students);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("email")) {
                Candidate candidate = candidateDao.findCandidateByEmail(searchValue).orElseThrow(() ->
                        new CandidateNotFoundException("Candidate Not Found for given Email "+searchValue));

                ResponseStructure<Candidate> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Candidate Found Successfully for given Email");
                res.setBody(candidate);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("phone")) {
                Candidate candidate = candidateDao.findCandidateByPhone(searchValue).orElseThrow(() ->
                        new CandidateNotFoundException("Candidate Not Found For Given Phone Number: "+searchValue));

                ResponseStructure<Candidate> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("Candidate Found Successfully for given Phone Number");
                res.setBody(candidate);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("subject")) {
                Subject subject = Subject.valueOf(searchValue.toUpperCase());

                Pageable pageable = PageRequest.of(page, limit, Sort.by("name").ascending());
                Page<Candidate> candidates = candidateDao.findCandidatesBySubjectName(subject, pageable);

                if (candidates.isEmpty())
                {
                    throw new CandidateNotFoundException("Students Not Found For Given Subject Name");
                }

                ResponseStructure<Page<Candidate>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Candidates Found Successfully For Given Subject Name");
                res.setBody(candidates);

                return ResponseEntity.ok(res);
            }
        }
        else if (searchValue.equalsIgnoreCase("quiz_completed"))
        {
            Pageable pageable = PageRequest.of(page, limit);

            Page<QuizInvite> invites = quizInviteDao.findQuizInvitesByUsedTrue(pageable);

            if (invites.isEmpty())
            {
                throw new QuizInviteNotFoundException("No Quiz Invites Found with Used True");
            }

            List<String> candidateIds = invites.getContent()
                    .stream()
                    .map(QuizInvite::getCandidateId)
                    .collect(Collectors.toList());

            List<Candidate> candidates = candidateDao.findAllCandidatesById(candidateIds);

            if (candidates.isEmpty())
            {
                throw new CandidateNotFoundException("No Candidates Found for Quiz Invites with Used True");
            }

            Pageable candidatePageable = PageRequest.of(page, limit, Sort.by("name").ascending());

            Page<Candidate> candidatePage = new PageImpl<>(
                    candidates,
                    candidatePageable,
                    invites.getTotalElements()
            );

            ResponseStructure<Page<Candidate>> res = new ResponseStructure<>();
            res.setStatus(HttpStatus.OK.value());
            res.setMessage("All Quiz Completed Candidates Found Successfully");
            res.setBody(candidatePage);

            return ResponseEntity.ok(res);
        }

        Pageable pageable = PageRequest.of(page, limit);

        Page<QuizInvite> invites = quizInviteDao.findQuizInvitesByUsedFalse(pageable);

        if (invites.isEmpty())
        {
            throw new QuizInviteNotFoundException("No Quiz Invites Found with Used False");
        }

        List<String> candidateIds = invites.getContent()
                .stream()
                .map(QuizInvite::getCandidateId)
                .collect(Collectors.toList());

        List<Candidate> candidates = candidateDao.findAllCandidatesById(candidateIds);

        if (candidates.isEmpty())
        {
            throw new CandidateNotFoundException("No Candidates Found for Quiz Invites with Used False");
        }

        Pageable candidatePageable = PageRequest.of(page, limit, Sort.by("name").ascending());

        Page<Candidate> candidatePage = new PageImpl<>(
                candidates,
                candidatePageable,
                invites.getTotalElements()
        );

        ResponseStructure<Page<Candidate>> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("All Quiz Pending Candidates Found Successfully");
        res.setBody(candidatePage);

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ResponseStructure<?>> getAllAnswerSets(String searchParam, String searchValue, int page, int limit) {
        if (searchParam.equalsIgnoreCase("candidateId"))
        {
            AnswerSet answerSet = answerSetDao.findAnswerSetByCandidateId(searchValue).orElseThrow(() ->
                    new AnswerSetNotFoundException("Answer Set Not Found For Given Candidate Id"));

            ResponseStructure<AnswerSet> res = new ResponseStructure<>();
            res.setStatus(HttpStatus.OK.value());
            res.setMessage("Answer Set Found For Given Candidate Id");
            res.setBody(answerSet);

            return ResponseEntity.ok(res);
        }
        Pageable pageable = PageRequest.of(page, limit, Sort.by("candidateId").ascending());
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

    @Override
    public ResponseEntity<ResponseStructure<User>> getUserByEmail(String email) {

        User user = userDao.getUserByEmail(email).orElseThrow(() ->
                new UserNotFoundException("User Not Found for given Email"));

        ResponseStructure<User> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("User Found Successfully for given Email");
        res.setBody(user);

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ResponseStructure<QuizInvite>> getQuizInviteByCandidateId(String candidateId) {

        QuizInvite invite = quizInviteDao.findByCandidateId(candidateId).orElseThrow(() ->
                new QuizInviteNotFoundException("Quiz Invite Not Found for given Candidate Id"));

        ResponseStructure<QuizInvite> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Quiz Invite Found Successfully for given Candidate Id");
        res.setBody(invite);

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ResponseStructure<?>> findAllQuestions(String searchParam, String searchValue, int page, int limit) {

        if (searchParam.equalsIgnoreCase("questionid") || searchParam.equalsIgnoreCase("questiontext") ||
                searchParam.equalsIgnoreCase("questiontype") || searchParam.equalsIgnoreCase("subject")) {

            if (searchParam.equalsIgnoreCase("questionid")) {
                Optional<Question> questionOpt = questionDao.findByQuestionId(searchValue);
                if (questionOpt.isEmpty()) {
                    throw new QuestionsNotFoundException("Question not found with id: " + searchValue);
                }

                ResponseStructure<Question> response = new ResponseStructure<>();
                response.setBody(questionOpt.get());
                response.setMessage("Question retrieved successfully");
                response.setStatus(HttpStatus.OK.value());

                return ResponseEntity.ok(response);
            }
            else if (searchParam.equalsIgnoreCase("questiontext")) {
                Pageable pageable = PageRequest.of(page, limit);
                Page<Question> questions = questionDao.findAllQuestionsByQuestionText(searchValue, pageable);

                if (questions.isEmpty()) {
                    throw new QuestionsNotFoundException("No Questions Found for Given Question Text");
                }

                ResponseStructure<Page<Question>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Questions Found Successfully for Given Question Text");
                res.setBody(questions);

                return ResponseEntity.ok(res);
            }
            else if (searchParam.equalsIgnoreCase("questiontype")) {

                QuestionType questionType = QuestionType.valueOf(searchValue.toUpperCase());

                Pageable pageable = PageRequest.of(page, limit);
                Page<Question> questions = questionDao.findByQuestionType(questionType, pageable);

                if (questions.isEmpty())
                {
                    throw new QuestionsNotFoundException("No Questions Found for Given Question Type");
                }

                ResponseStructure<Page<Question>> response = new ResponseStructure<>();
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("All Questions Found Successfully for Given Question Type");
                response.setBody(questions);

                return ResponseEntity.ok(response);
            }
            else if (searchParam.equalsIgnoreCase("subject")) {

                Subject subject = Subject.valueOf(searchValue);

                Pageable pageable = PageRequest.of(page, limit);
                Page<Question> questions = questionDao.findAllQuestionsBySubject(subject, pageable);

                if (questions.isEmpty())
                {
                    throw new QuestionsNotFoundException("No Questions Found for Given Subject");
                }

                ResponseStructure<Page<Question>> res = new ResponseStructure<>();
                res.setStatus(HttpStatus.OK.value());
                res.setMessage("All Questions Found Successfully for Given Subject");
                res.setBody(questions);

                return ResponseEntity.ok(res);
            }
        }

        Pageable pageable = PageRequest.of(page, limit);
        Page<Question> questionPage = questionDao.findAllQuestions(pageable);

        ResponseStructure<Page<Question>> response = new ResponseStructure<>();
        response.setBody(questionPage);
        response.setMessage("All Questions retrieved successfully");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    //    PUT  Methods--------------------------------------------------------------------------

    @Override
    public ResponseEntity<EnrollmentResponseDTO> updateQuizTimeLimit(String candidateId, String timeLimitInMinutes) {

        Candidate candidate = candidateDao.findCandidateById(candidateId).orElseThrow(() ->
                new CandidateNotFoundException("Candidate Not Found for given Id"));

        QuizInvite quizInvite = quizInviteDao.findByCandidateId(candidateId).orElseThrow(() ->
                new QuizInviteNotFoundException("Quiz Invite Not Found for given Candidate Id"));

        String timeLimitInMinutesStr = timeLimitInMinutes + " minutes";
        quizInvite.setQuizTimeLimit(timeLimitInMinutesStr);

        quizInviteDao.save(quizInvite);

        String link = quizFrontendUrl + "?token=" + quizInvite.getToken();
        EnrollmentResponseDTO enrollmentResponseDTO = new EnrollmentResponseDTO();
        enrollmentResponseDTO.setCandidateId(quizInvite.getCandidateId());
        enrollmentResponseDTO.setSubject(quizInvite.getSubject());
        enrollmentResponseDTO.setInviteLink(link);
        enrollmentResponseDTO.setQuizTimeLimit(quizInvite.getQuizTimeLimit());

        // Try to email the invite link to the candidate; do not fail enrollment if email sending fails
        try {
            if (candidate.getEmail() != null && !candidate.getEmail().isBlank()) {
                inviteEmailSenderService.sendInviteEmail(
                        candidate.getEmail(),
                        candidate.getName(),
                        candidate.getSubject(),
                        link
                );
            }
        } catch (MessagingException e) {
            // Swallow or log; keeping enrollment successful.
            e.printStackTrace();
        }

        return ResponseEntity.ok(enrollmentResponseDTO);
    }

    //    DELETE Methods------------------------------------------------------------------------
    @Override
    public ResponseEntity<ResponseStructure<String>> deleteCandidate(String candidateId) {

        Candidate candidate = candidateDao.findCandidateById(candidateId).orElseThrow(() ->
                new CandidateNotFoundException("Candidate Not Found for given Id"));

        QuizInvite invite = quizInviteDao.findByCandidateId(candidateId).orElseThrow(() ->
                new QuizInviteNotFoundException("Quiz Invite Not Found for given Candidate Id"));

        if (!invite.getUsed())
        {
            throw new CannotDeleteCandidateException("Cannot delete candidate; quiz not yet taken");
        }

        AnswerSet answerSet = answerSetDao.findAnswerSetByCandidateId(candidateId).orElseThrow(() ->
                new AnswerSetNotFoundException("Answer Set Not Found for given Candidate Id"));

        answerSetDao.deleteAnswerSet(answerSet);

        quizInviteDao.deleteQuizInvite(invite);

        candidateDao.deleteCandidate(candidate);

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("Candidate Deleted Successfully for given Id");
        res.setBody("Candidate with Id "+candidateId+" deleted successfully");

        return ResponseEntity.ok(res);
    }
}
