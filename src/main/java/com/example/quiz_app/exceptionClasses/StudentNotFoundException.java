package com.example.quiz_app.exceptionClasses;

public class StudentNotFoundException extends RuntimeException {

    private final String msg;

    public StudentNotFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
