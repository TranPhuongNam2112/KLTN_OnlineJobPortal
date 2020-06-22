package com.datn.onlinejobportal.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.dto.UserProfile;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.WebsiteStats;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	@Qualifier("sessionRegistry")
	private SessionRegistry sessionRegistry;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private JobPostRepository jobPostRepository;

	@GetMapping("/users")
	@RolesAllowed("ROLE_ADMIN")
	public Page<UserProfile> getAllUsers(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy
			) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.getAllUsers(pageable);
	}



	@GetMapping("/users/candidates")
	@RolesAllowed("ROLE_ADMIN")
	public Page<User> getAllCandidates(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy
			) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.findAllCandidates(pageable);
	}

	@GetMapping("/users/employers")
	@RolesAllowed("ROLE_ADMIN")
	public Page<User> getAllEmployers(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy
			) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.findAllEmployers(pageable);
	}
	
	@GetMapping("/websitetats")
	@RolesAllowed("ROLE_ADMIN")
	public WebsiteStats getOverallStats() {
		List<Object> principals = sessionRegistry.getAllPrincipals();

		List<String> usersNamesList = new ArrayList<String>();
		for (Object principal: principals) {
		    if (principal instanceof User) {
		        usersNamesList.add(((User) principal).getName());
		    }
		}
		WebsiteStats webstats = new WebsiteStats();
		webstats.setCountAllCandidates(candidateRepository.getCandidateCounts());
		webstats.setCountAllEmployers(employerRepository.getAllEmployerCounts());
		webstats.setCountNewCandidatesToday(candidateRepository.getCurrentDateNewCandidatesCount());
		webstats.setCountNewJobPostsToday(jobPostRepository.getNewJobPostsCountToday());
		webstats.setCountNewRegisteredEmployerToday(employerRepository.getNewRegisteredEmployerCountsToday());
		webstats.setCountRegisteredEmployers(employerRepository.getRegisteredEmployerCounts());
		webstats.setCurrentUsersOnline(Long.valueOf(usersNamesList.size()));
		return webstats;
	}
}
