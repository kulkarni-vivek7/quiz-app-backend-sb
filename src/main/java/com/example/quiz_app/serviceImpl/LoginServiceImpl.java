package com.example.quiz_app.serviceImpl;

import com.example.quiz_app.config.JwtUtil;
import com.example.quiz_app.dao.LoginDao;
import com.example.quiz_app.dao.UserDao;
import com.example.quiz_app.dto.LoginRequestDTO;
import com.example.quiz_app.dto.UserRegistrationDTO;
import com.example.quiz_app.enums.Status;
import com.example.quiz_app.enums.UserRole;
import com.example.quiz_app.exceptionClasses.*;
import com.example.quiz_app.models.LoginDetails;
import com.example.quiz_app.models.User;
import com.example.quiz_app.otpMailSender.OtpEmailSenderService;
import com.example.quiz_app.response.ResponseStructure;
import com.example.quiz_app.service.LoginService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class LoginServiceImpl implements LoginService {

    private final LoginDao loginDao;

    private final UserDao userDao;

    private final JwtUtil jwtUtil;

    private final OtpEmailSenderService service;

    private static final Long OTP_VALID_DURATION = 10L;

    public LoginServiceImpl(LoginDao loginDao, UserDao userDao, JwtUtil jwtUtil, OtpEmailSenderService service) {
        this.loginDao = loginDao;
        this.userDao = userDao;
        this.jwtUtil = jwtUtil;
        this.service = service;
    }

    @Override
    public ResponseEntity<ResponseStructure<User>> userRegistration(UserRegistrationDTO userRegistrationDTO) {

        if (loginDao.existsByRoleHr(UserRole.HR))
        {
            throw new HrAlreadyExistsException("Cannot Register More Than HR Details");
        }

        User user = new User();
        user.setName(userRegistrationDTO.getName());
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPhone(userRegistrationDTO.getPhone());
        user.setRole(UserRole.HR);

        user = userDao.saveUser(user);

        LoginDetails loginDetails = new LoginDetails();
        loginDetails.setEmail(user.getEmail());
        loginDetails.setPhone(user.getPhone());
        loginDetails.setRole(user.getRole());
        loginDetails.setStatus(Status.ACTIVE);
        loginDetails.setTimeStamp(LocalDateTime.now());
        loginDetails.setIsLoggedIn(false);

        loginDao.saveLoginDetails(loginDetails);

        ResponseStructure<User> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.CREATED.value());
        res.setMessage("HR Details Registered Successfully");
        res.setBody(user);;

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ResponseStructure<String>> sendOtpEmail(String email) {

        LoginDetails loginDetails = loginDao.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException("No User Found For The Email: " + email));

        if (loginDetails.getStatus() == Status.INACTIVE)
        {
            throw new InactiveUserAccountException("Inactive Account!... Unable to Send OTP Email");
        }

        String otp = service.generateOtp();

        loginDetails.setOtp(otp);
        loginDetails.setTimeStamp(LocalDateTime.now());
        loginDetails.setIsLoggedIn(false);
        loginDao.updateLoginDetails(loginDetails);

        try {
            service.sendOtpEmail(email, otp);
        }
        catch (MessagingException e)
        {
            throw new RuntimeException("Failed to Send OTP Email");
        }

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage("OTP Sent Successfully");
        res.setBody("OTP has been sent to your Email");

        return ResponseEntity.ok(res);
    }

    @Override
    public ResponseEntity<ResponseStructure<String>> loginWithOtp(LoginRequestDTO loginRequestDTO) {

        LoginDetails loginDetails = loginDao.findByEmail(loginRequestDTO.getEmail()).orElseThrow(() ->
                new UserNotFoundException("User with the given email not found"));

        if (loginDetails.getStatus() == Status.INACTIVE)
        {
            throw new InactiveUserAccountException("Inactive Account!... Unable to Login");
        }

        if (!loginRequestDTO.getOtp().equals(loginDetails.getOtp()))
        {
            throw new InvalidOtpException("Invalid OTP... Unable to Login");
        }

        if (Boolean.TRUE.equals(loginDetails.getIsLoggedIn()))
        {
            throw new OtpAlreadyUsedException("OTP Already Used... Generate a New OTP for Login");
        }

        long minutesPassed = ChronoUnit.MINUTES.between(loginDetails.getTimeStamp(), LocalDateTime.now());
        if (minutesPassed > OTP_VALID_DURATION)
        {
            throw new OtpExpiredException("OTP Expired... Generate a New OTP");
        }

        String token = jwtUtil.generateToken(loginDetails.getEmail());
        loginDetails.setIsLoggedIn(true);
        loginDao.updateLoginDetails(loginDetails);

        String message = loginDetails.getRole() + " Logged In Successfully";

        ResponseStructure<String> res = new ResponseStructure<>();
        res.setStatus(HttpStatus.OK.value());
        res.setMessage(message);
        res.setBody(token);

        return ResponseEntity.ok(res);
    }
}