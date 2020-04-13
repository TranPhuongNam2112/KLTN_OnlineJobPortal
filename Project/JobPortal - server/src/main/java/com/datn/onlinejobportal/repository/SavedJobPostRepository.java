package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.SavedJobPost;

public interface SavedJobPostRepository extends JpaRepository<SavedJobPost, Long>{
	
	long countByCandidate_id(Long candidate_id);

}
