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
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.imageUrl, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary, j.sourceUrl) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "LEFT JOIN j.savedjobpost p "
			+ "Where p.candidate.id = :candidateId")
	Page<JobPostSummary> getJobPostsSavedBy(@Param("candidateId") Long candidateId, Pageable pageable); 

	
	@Query("Select sjp from SavedJobPost sjp Where sjp.jobpost.id = :jobpostId And sjp.candidate.id = :candidateId")
	SavedJobPost getSavedJobPostByJobPostId(@Param("candidateId") Long candidateId, @Param("jobpostId") Long jobpostId);
}
