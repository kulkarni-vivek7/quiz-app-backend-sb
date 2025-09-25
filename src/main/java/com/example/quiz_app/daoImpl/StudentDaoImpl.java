package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.StudentDao;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Student;
import com.example.quiz_app.repository.InMemoryStudentRepository;
import com.example.quiz_app.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class StudentDaoImpl implements StudentDao {

    private final InMemoryStudentRepository repo;
    private final StudentRepository studentRepository;

    public StudentDaoImpl(InMemoryStudentRepository repo, StudentRepository studentRepository) {
        this.repo = repo;
        this.studentRepository = studentRepository;
    }

    @Override
    public void saveInHashMap(Student student) {
        repo.saveInHashMap(student);
    }

    @Override
    public Student saveInMongoDb(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Optional<Student> findStudentById(String searchValue) {
        return studentRepository.findById(searchValue);
    }

    @Override
    public Page<Student> findStudentsByName(String searchValue, Pageable pageable) {
        return studentRepository.findByNameContainingIgnoreCase(searchValue, pageable);
    }

    @Override
    public Optional<Student> findStudentByEmail(String searchValue) {
        return studentRepository.findByEmail(searchValue);
    }

    @Override
    public Optional<Student> findStudentByPhone(String searchValue) {
        return studentRepository.findByPhone(searchValue);
    }

    @Override
    public Page<Student> findStudentsBySubjectName(Subject subject, Pageable pageable) {
        return studentRepository.findAllBySubject(subject, pageable);
    }

    @Override
    public Page<Student> findAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }
}
