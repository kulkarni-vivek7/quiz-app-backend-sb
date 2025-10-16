package com.example.quiz_app.repository;

import com.example.quiz_app.models.Candidate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class InMemoryStudentRepository {
    private final Map<String, Candidate> students = new HashMap<>();

    public void saveInHashMap(Candidate candidate) {
        students.put(candidate.getId(), candidate);
    }
}
