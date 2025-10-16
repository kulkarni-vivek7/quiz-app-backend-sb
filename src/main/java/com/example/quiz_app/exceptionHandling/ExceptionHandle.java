package com.example.quiz_app.exceptionHandling;


import com.example.quiz_app.exceptionClasses.*;
import com.example.quiz_app.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(NoAnswerSetFoundException.class)
    public ResponseEntity<ErrorResponse> noAnswerSetFoundExceptionHandler(NoAnswerSetFoundException nasf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(nasf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CandidateNotFoundException.class)
    public ResponseEntity<ErrorResponse> studentNotFoundExceptionHandler(CandidateNotFoundException snf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(snf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AnswerSetNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerSetNotFoundExceptionHandler(AnswerSetNotFoundException asnf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(asnf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuestionsNotFoundException.class)
    public ResponseEntity<ErrorResponse> questionsNotFoundExceptionHandler(QuestionsNotFoundException qnf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(qnf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LoginDetailsNotFoundException.class)
    public ResponseEntity<ErrorResponse> loginDetailsNotFoundExceptionHandler(LoginDetailsNotFoundException ldnf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(ldnf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(UserNotFoundException unf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(unf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(QuizInviteNotFoundException.class)
    public ResponseEntity<ErrorResponse> quizInviteNotFoundExceptionHandler(QuizInviteNotFoundException qinf)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(qinf.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    BAD_REQUEST Exceptions

    @ExceptionHandler(InvalidInviteTokenException.class)
    public ResponseEntity<ErrorResponse> invalidInviteTokenExceptionHandler(InvalidInviteTokenException ex)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HrAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> hrAlreadyExistsExceptionHandler(HrAlreadyExistsException hae)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(hae.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InactiveUserAccountException.class)
    public ResponseEntity<ErrorResponse> inactiveUserAccountExceptionHandler(InactiveUserAccountException iua)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(iua.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOtpException.class)
    public ResponseEntity<ErrorResponse> invalidOtpExceptionHandler(InvalidOtpException io)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(io.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> otpAlreadyUsedExceptionHandler(OtpAlreadyUsedException oau)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(oau.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorResponse> otpExpiredExceptionHandler(OtpExpiredException oe)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(oe.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InviteTokenAlreadyUsedException.class)
    public ResponseEntity<ErrorResponse> inviteTokenAlreadyUsedExceptionHandler(InviteTokenAlreadyUsedException itau)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(itau.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CannotDeleteCandidateException.class)
    public ResponseEntity<ErrorResponse> cannotDeleteCandidateExceptionHandler(CannotDeleteCandidateException cdc)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(cdc.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException eae)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(eae.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> phoneNumberAlreadyExistsExceptionHandler(PhoneNumberAlreadyExistsException pnae)
    {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setMessage(pnae.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}