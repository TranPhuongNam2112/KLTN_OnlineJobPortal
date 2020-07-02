package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.model.CandidateApplication;

public interface CandidateApplicationRepository extends JpaRepository<CandidateApplication, Long> {

	@Query("Select new com.datn.onlinejobportal.dto.CandidateSummary(c.id, f.data, u.name, c.city_province, c.work_title, c.updatedAt, u.imageUrl) From Candidate c "
			+ "LEFT JOIN c.savedCandidates sc "
			+ "LEFT JOIN c.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN c.candidateapplications ca "
			+ "Where ca.jobpost.employer.id = :employerId")
	List<CandidateSummary> getAllAppliedCandidates(@Param("employerId") Long employerId); 
}
