package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.model.CandidateHistory;
import com.datn.onlinejobportal.model.JobPost;

public interface CandidateHistoryRepository extends JpaRepository<CandidateHistory, Long>{
	
	@Query("Select jp From Candidate c "
			+ "LEFT JOIN c.candidatehistories ch "
			+ "LEFT JOIN ch.jobpost jp "
			+ "LEFT JOIN c.user u "
			+ "Where jp.id = :jobpostId And u.id = :userId")
	JobPost getDuplicateViewedJobPost(@Param("jobpostId") Long jobpostId, @Param("userId") Long userId);
	
	@Query("Select ch From Candidate c "
			+ "LEFT JOIN c.candidatehistories ch "
			+ "LEFT JOIN c.user u "
			+ "Where ch.jobpost.id =:jobpostId And u.id =:userId")
	CandidateHistory getCandidateHistory(@Param("jobpostId") Long jobpostId, @Param("userId") Long userId);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.candidatehistories ch "
			+ "Where j.expirationDate >= CURRENT_DATE AND ch.candidate.id = :candidateId ORDER BY j.expirationDate DESC")
	List<JobPostSummary> getAllCandidatHistory(@Param("candidateId") Long candidateId);
}
