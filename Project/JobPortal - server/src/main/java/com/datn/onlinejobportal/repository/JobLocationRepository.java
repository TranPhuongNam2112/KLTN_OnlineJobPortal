package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.JobLocation;

public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {
	
	@Query("SELECT jl FROM JobLocation jl LEFT JOIN jl.jobpost jp WHERE jp.id = :jobpostId")
	JobLocation findByJobPostId(@Param("jobpostId") Long jobpostId);

	@Query("Select distinct jl.city_province FROM JobLocation jl")
	List<String> getAllJobLocations();
}
