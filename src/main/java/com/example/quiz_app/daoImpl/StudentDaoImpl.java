package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.StudentDao;
import com.example.quiz_app.models.Student;
import com.example.quiz_app.repository.InMemoryStudentRepository;
import org.springframework.stereotype.Component;

@Component
public class StudentDaoImpl implements StudentDao {

    private final InMemoryStudentRepository repo;

    public StudentDaoImpl(InMemoryStudentRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(Student student) {
        repo.save(student);
    }
}
