package com.datn.onlinejobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.SavedJobPostRepository;

@Service
public class CandidateService {
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private SavedJobPostRepository savedJobPostRepository;

}
