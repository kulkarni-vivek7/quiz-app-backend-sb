package com.example.quiz_app.dao;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentDao {
    void saveInHashMap(Student student);

    void saveInMongoDb(Student student);

    Optional<Student> findStudentById(String searchValue);

    Page<Student> findStudentsByName(String searchValue, Pageable pageable);

    Optional<Student> findStudentByEmail(String searchValue);

    Optional<Student> findStudentByPhone(String searchValue);

    Page<Student> findStudentsBySubjectName(Subject subject, Pageable pageable);

    Page<Student> findAllStudents(Pageable pageable);
}
