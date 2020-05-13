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
	

	@Query("Select new com.datn.onlinejobportal.dto.MyJobPostSummary(jp.id, jp.job_title, jl.city_province, jt.job_type_name, jp.requiredexperienceyears, jp.expirationDate, jp.min_salary, jp.max_salary) "
			+ "From JobPost jp "
			+ "LEFT JOIN jp.savedjobpost sjp "
			+ "LEFT JOIN jp.joblocation jl "
			+ "LEFT JOIN jp.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN jp.jobtype jt "
			+ "Where e.id = :employerId")
	Page<MyJobPostSummary> getAllJobPostByEmployerId(@Param("employerId") Long employerId, Pageable pageable);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where jt.job_type_name = :jobtypename AND j.expirationDate >= CURRENT_DATE")
	Page<JobPostSummary> getJobPostsByJobType(@Param("jobtypename") String jobtypename, Pageable pageable); 
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where jt.job_type_name IN (SELECT jt.job_type_name FROM Candidate c LEFT JOIN c.jobtypes jt WHERE c.id = :candidateId ) AND "
			+ "j.max_salary <= (SELECT c.expectedsalary FROM Candidate c WHERE c.id = :candidateId) AND j.min_salary >= (SELECT c.expectedsalary FROM Candidate c WHERE c.id = :candidateId) "
			+ "AND jl.city_province = (SELECT c.city_province FROM Candidate c WHERE c.id = :candidateId) AND j.expirationDate >= CURRENT_DATE")
	Page<JobPostSummary> getRecommendedJobPostsByUser(@Param("candidateId") Long candidateId, Pageable pageable); 
	
	@Query("Select j from JobPost j Where j.id = :jobpostId")
	JobPost findByJobPostId(@Param("jobpostId") Long jobpostId);
	
	@Query("Select new com.datn.onlinejobportal.dto.JobPostSummary(j.id, f.data, e.companyname, j.job_title, j.requiredexperienceyears, jl.city_province, jt.job_type_name, j.expirationDate, j.min_salary, j.max_salary) "
			+ "From JobPost j "
			+ "LEFT JOIN j.employer e "
			+ "LEFT JOIN e.user u "
			+ "LEFT JOIN u.files f "
			+ "LEFT JOIN j.joblocation jl "
			+ "LEFT JOIN j.jobtype jt "
			+ "Where j.expirationDate >= CURRENT_DATE")
	Page<JobPostSummary> getAllJobPosts(Pageable pageable); 
}
