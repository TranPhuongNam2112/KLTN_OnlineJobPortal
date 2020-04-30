package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.JobType;

public interface JobTypeRepository extends JpaRepository<JobType, Long> {
	
	JobType findByJob_type_name(String job_type_name);

}
