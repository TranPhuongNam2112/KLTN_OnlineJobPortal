package com.datn.onlinejobportal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.dto.MyJobPostSummary;
import com.datn.onlinejobportal.model.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost, Long>, JpaSpecificationExecutor<JobPost> {

	@Query("Select new com.datn.onlinejobportal.dto.MyJobPostSummary(jp.job_title, jl.city_province, jt.job_type_name, jp.expirationDate, jp.min_salary, jp.max_salary) "
			+ "From JobPost jp "
			+ "LEFT JOIN jp.savedjobpost sjp "
			+ "LEFT JOIN jp.joblocation jl "
			+ "LEFT JOIN jp.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN jp.jobtype jt "
			+ "Where e.id = :employerId")
	Page<MyJobPostSummary> getAllJobPostByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
}
