package com.datn.onlinejobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;

@Service
public class StatisticService {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	

	

}
