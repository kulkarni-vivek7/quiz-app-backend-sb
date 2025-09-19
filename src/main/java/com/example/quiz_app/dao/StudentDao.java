package com.example.quiz_app.dao;

import com.example.quiz_app.models.Student;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDao {
    void save(Student student);
}
