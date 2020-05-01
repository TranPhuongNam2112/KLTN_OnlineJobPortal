package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.datn.onlinejobportal.model.JobType;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
	
	@Query("Select j FROM JobType j where j.job_type_name = ?1")
	JobType findByJob_type_name(String job_type_name);

}
