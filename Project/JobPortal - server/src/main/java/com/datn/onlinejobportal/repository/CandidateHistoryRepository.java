package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}
