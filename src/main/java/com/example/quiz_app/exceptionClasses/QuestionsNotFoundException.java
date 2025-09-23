package com.example.quiz_app.exceptionClasses;

public class QuestionsNotFoundException extends RuntimeException {

    private final String msg;

    public QuestionsNotFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
