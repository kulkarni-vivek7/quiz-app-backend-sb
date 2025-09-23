package com.example.quiz_app.exceptionClasses;

public class NoAnswerSetFoundException extends RuntimeException {

    private final String msg;

    public NoAnswerSetFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
