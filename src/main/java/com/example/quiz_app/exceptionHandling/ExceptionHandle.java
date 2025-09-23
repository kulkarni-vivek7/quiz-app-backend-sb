package com.example.quiz_app.exceptionHandling;


import com.example.quiz_app.exceptionClasses.AnswerSetNotFoundException;
import com.example.quiz_app.exceptionClasses.NoAnswerSetFoundException;
import com.example.quiz_app.exceptionClasses.QuestionsNotFoundException;
import com.example.quiz_app.exceptionClasses.StudentNotFoundException;
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

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> studentNotFoundExceptionHandler(StudentNotFoundException snf)
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
}
