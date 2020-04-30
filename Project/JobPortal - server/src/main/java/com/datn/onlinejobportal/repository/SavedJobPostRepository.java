package com.datn.onlinejobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.model.SavedJobPost;

public interface SavedJobPostRepository extends JpaRepository<SavedJobPost, Long>{
	
	Long countByCandidate_id(Long candidate_id);
	
	@Query(value="Select f.data, e.companyname, jp.job_title, jt.city_province, jt.jop_type_name, jp.expirationDate, jp.min_salary, jp.max_salary from JobPost jp"
			+ "Join jp.employer e "
			+ "Join e.user u "
			+ "Join u.files f "
			+ "Join jp.jobtype jt "
			+ "Join jp.joblocation jl "
			+ "Join jp.savedjobpost sjp"
			+ "Where sjp.candidateId = :candidateId", nativeQuery=true)
	Page<JobPostSummary> getJobPostsSavedBy(@Param("candidateId") Long candidateId, Pageable pageable); 

}
