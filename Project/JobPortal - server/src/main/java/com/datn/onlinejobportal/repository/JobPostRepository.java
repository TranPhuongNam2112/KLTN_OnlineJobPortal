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

	@Query(value="Select jp.job_title, jl.city_province, jt.job_type_name, jp.expirationDate, jp.min_salary, jp.max_salary "
			+ "From JobPost jp"
			+ "Join jp.savedjobpost sjp"
			+ "Join jp.joblocation jl"
			+ "Join jp.employer e"
			+ "Join e.user u"
			+ "Join u.files f"
			+ "Join jp.jobtype jt"
			+ "Where e.id = :employerId", nativeQuery=true)
	Page<MyJobPostSummary> getAllJobPostByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
}
