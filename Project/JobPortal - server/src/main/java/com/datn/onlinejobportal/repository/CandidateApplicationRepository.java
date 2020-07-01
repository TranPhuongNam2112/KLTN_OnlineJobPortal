package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.CandidateApplication;

public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, Long> {

}
