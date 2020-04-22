package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.datn.onlinejobportal.model.JobLocation;

public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {
	
	@Query(value="Select j from JobLocation j JOIN j.jobpost jp where jp.id = ?1", nativeQuery=true)
	JobLocation findByJobPostId(Long jobpostId);

}
