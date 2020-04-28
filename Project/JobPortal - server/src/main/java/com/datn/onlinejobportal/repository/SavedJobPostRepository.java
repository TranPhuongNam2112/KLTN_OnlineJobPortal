package com.datn.onlinejobportal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.SavedJobPost;

public interface SavedJobPostRepository extends JpaRepository<SavedJobPost, Long>{
	
	Long countByCandidate_id(Long candidate_id);
	
	@Query(value="Select jp from JobPost jp Join jp.savedjobpost sjp Join sjp.candidate c Join c.user u Where u.id = :userId and jp.id in :jobpostIds", nativeQuery=true)
	List<JobPost> findByUserIdAndJobPostIdIn(@Param("userId") Long userId, @Param("jobpostIds") List<Long> jobpostsIds); 

}
