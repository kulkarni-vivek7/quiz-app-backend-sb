package com.example.quiz_app.exceptionClasses;

public class AnswerSetNotFoundException extends RuntimeException {
    private final String msg;

    public AnswerSetNotFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
