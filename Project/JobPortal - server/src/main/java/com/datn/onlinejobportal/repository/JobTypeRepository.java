package com.datn.onlinejobportal.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.JobType;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
	
	@Query("Select j FROM JobType j where j.job_type_name = ?1")
	JobType findByJob_type_name(String job_type_name);
	
	@Query("Select j.job_type_name FROM Candidate c LEFT JOIN c.jobtypes j WHERE c.id = :candidateId")
	List<String> getAllCandidateJobTypeName(@Param("candidateId") Long candidateId);
	
	@Query("Select j FROM JobType j WHERE j.job_type_name IN :jobtypenames")
	Set<JobType> getAllCandidateJobType(@Param("jobtypenames") List<String> jobtypenames);
	
	@Query("Select j.job_type_name FROM JobType j")
	List<String> getAllJobTypes();
	
	@Query("Select j.job_type_name From JobPost jp LEFT JOIN jp.jobtype j Where jp.id = :jobpostId")
	String getAllJobPostTypeName(@Param("jobpostId") Long jobpostId);


}
