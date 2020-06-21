package com.datn.onlinejobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerHistoryRepository;
import com.datn.onlinejobportal.repository.SavedCandidateRepository;
import com.datn.onlinejobportal.security.UserPrincipal;

@Service
public class CandidateStatisticService {
	
	@Autowired
	private SavedCandidateRepository savedCandidateRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private EmployerHistoryRepository employerHistoryRepository;
	
	public Long countSavedProfileEmployers(UserPrincipal currentUser) {
		return savedCandidateRepository.countSavedProfileEmployer(candidateRepository.getCandidateIdByUserId(currentUser.getId()));
	}
	
	public Long countViewedProfileEmployers(UserPrincipal currentUser) {
		return employerHistoryRepository.countViewedProfileEmployer(candidateRepository.getCandidateIdByUserId(currentUser.getId()));
	}

}
