package com.example.quiz_app.daoImpl;

import com.example.quiz_app.dao.CandidateDao;
import com.example.quiz_app.enums.Subject;
import com.example.quiz_app.models.Candidate;
import com.example.quiz_app.repository.InMemoryStudentRepository;
import com.example.quiz_app.repository.CandidateRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CandidateDaoImpl implements CandidateDao {

    private final InMemoryStudentRepository repo;
    private final CandidateRepository candidateRepository;

    public CandidateDaoImpl(InMemoryStudentRepository repo, CandidateRepository candidateRepository) {
        this.repo = repo;
        this.candidateRepository = candidateRepository;
    }

    @Override
    public void saveInHashMap(Candidate candidate) {
        repo.saveInHashMap(candidate);
    }

    @Override
    public Candidate saveInMongoDb(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    @Override
    public Optional<Candidate> findCandidateById(String searchValue) {
        return candidateRepository.findById(searchValue);
    }

    @Override
    public Page<Candidate> findCandidatesByName(String searchValue, Pageable pageable) {
        return candidateRepository.findByNameContainingIgnoreCase(searchValue, pageable);
    }

    @Override
    public Optional<Candidate> findCandidateByEmail(String searchValue) {
        return candidateRepository.findByEmail(searchValue);
    }

    @Override
    public Optional<Candidate> findCandidateByPhone(String searchValue) {
        return candidateRepository.findByPhone(searchValue);
    }

    @Override
    public Page<Candidate> findCandidatesBySubjectName(Subject subject, Pageable pageable) {
        return candidateRepository.findAllBySubject(subject, pageable);
    }

    @Override
    public Page<Candidate> findAllCandidates(Pageable pageable) {
        return candidateRepository.findAll(pageable);
    }

    @Override
    public void deleteCandidate(Candidate candidate) {
        candidateRepository.delete(candidate);
    }

    @Override
    public List<Candidate> findAllCandidatesById(List<String> candidateIds) {
        return candidateRepository.findAllById(candidateIds);
    }
}
