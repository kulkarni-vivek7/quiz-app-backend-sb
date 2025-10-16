package com.example.quiz_app.exceptionClasses;

public class LoginDetailsNotFoundException extends RuntimeException {

    private String msg;

    public LoginDetailsNotFoundException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
