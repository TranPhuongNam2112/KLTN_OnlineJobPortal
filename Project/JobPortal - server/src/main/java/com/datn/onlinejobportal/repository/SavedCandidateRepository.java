package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.SavedCandidate;

public interface SavedCandidateRepository extends JpaRepository<SavedCandidate, Long>{

}
