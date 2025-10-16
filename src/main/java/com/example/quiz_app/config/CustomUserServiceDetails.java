package com.example.quiz_app.config;

import com.example.quiz_app.dao.LoginDao;
import com.example.quiz_app.exceptionClasses.LoginDetailsNotFoundException;
import com.example.quiz_app.models.LoginDetails;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Log
public class CustomUserServiceDetails implements UserDetailsService {

    private final LoginDao loginDao;

    public CustomUserServiceDetails(LoginDao loginDao) {
        this.loginDao = loginDao;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        LoginDetails loginDetails = loginDao.findByEmail(email).orElseThrow(() ->
                new LoginDetailsNotFoundException("Login Details Not Found For Given Email Id"));

        return User
                .withUsername(loginDetails.getEmail())
                .password(loginDetails.getOtp())
                .roles(loginDetails.getRole() != null ? loginDetails.getRole().name() : "USER")
                .build();
    }
}