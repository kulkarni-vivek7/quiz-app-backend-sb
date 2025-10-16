package com.example.quiz_app.dao;

import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CandidateDao {
    void saveInHashMap(Candidate candidate);

    Candidate saveInMongoDb(Candidate candidate);

    Optional<Candidate> findCandidateById(String searchValue);

    Page<Candidate> findCandidatesByName(String searchValue, Pageable pageable);

    Optional<Candidate> findCandidateByEmail(String searchValue);

    Optional<Candidate> findCandidateByPhone(String searchValue);

    Page<Candidate> findCandidatesBySubjectName(Subject subject, Pageable pageable);

    Page<Candidate> findAllCandidates(Pageable pageable);

    void deleteCandidate(Candidate candidate);

    List<Candidate> findAllCandidatesById(List<String> candidateIds);
}
