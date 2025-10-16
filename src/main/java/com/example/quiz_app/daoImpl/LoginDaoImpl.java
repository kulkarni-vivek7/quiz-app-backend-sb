package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.LoginDao;
import com.example.quiz_app.enums.UserRole;
import com.example.quiz_app.models.LoginDetails;
import com.example.quiz_app.repository.LoginRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoginDaoImpl implements LoginDao {

    private final LoginRepository loginRepository;

    public LoginDaoImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public Optional<LoginDetails> findByEmail(String email) {
        return loginRepository.findFirstByEmail(email);
    }

    @Override
    public boolean existsByRoleHr(UserRole role) {
        return loginRepository.existsByRole(role);
    }

    @Override
    public void saveLoginDetails(LoginDetails loginDetails) {
        loginRepository.save(loginDetails);
    }

    @Override
    public void updateLoginDetails(LoginDetails loginDetails) {
        loginRepository.save(loginDetails);
    }

    @Override
    public Optional<LoginDetails> findByPhone(String phone) {
        return loginRepository.findFirstByPhone(phone);
    }
}
