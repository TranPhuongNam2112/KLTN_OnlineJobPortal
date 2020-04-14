package com.datn.onlinejobportal.service;

import org.springframework.stereotype.Service;

@Service
public class CandidateService {
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private SavedJobPostRepository savedJobPostRepository;

}
