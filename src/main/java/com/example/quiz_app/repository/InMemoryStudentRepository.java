package com.example.quiz_app.repository;

import com.example.quiz_app.models.Student;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryStudentRepository {
    private final Map<String, Student> students = new HashMap<>();

    public void save(Student student) {
        students.put(student.getId(), student);
    }
}
