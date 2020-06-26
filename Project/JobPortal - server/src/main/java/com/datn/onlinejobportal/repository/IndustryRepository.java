package com.datn.onlinejobportal.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.Industry;

public interface IndustryRepository extends JpaRepository<Industry, Long>{
	
	@Query("Select i From Industry i Where i.industryname IN :industries")
	Set<Industry> getAllIndustries(@Param("industries") List<String> industries);
	
	@Query("Select i.industryname From JobPost jp LEFT JOIN jp.industries i Where jp.id = :jobpostId")
	List<String> getAllJobPostIndustries(@Param("jobpostId") Long jobpostId);
	
	@Query("Select i From Industry i Where i.industryname In :names")
	Set<Industry> getAllIndustriesByNames(@Param("names") List<String> names);
	
	@Query("Select i.industryname From Candidate c "
			+ "LEFT JOIN c.industries i")
	List<String> getCandidateIndustryNames();
	
	@Query("Select i.industryname From Industry i")
	List<String> getAllIndustriesName();
	
	@Query("Select i.id From Industry i "
			+ "LEFT JOIN i.jobpost j "
			+ "GROUP BY i.id "
			+ "HAVING COUNT(j.id) = (SELECT MAX(COUNT(j.id)) FROM Industry i LEFT JOIN i.jobpost j)")
	Long getMostJobPostsIndustryId();
	
	
	
}
