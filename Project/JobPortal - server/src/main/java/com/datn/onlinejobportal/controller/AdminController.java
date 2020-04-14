package com.datn.onlinejobportal.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.payload.UserProfile;
import com.datn.onlinejobportal.repository.SavedJobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SavedJobPostRepository savedJobPostRepository;
	
	@GetMapping("/users")
	@RolesAllowed("ROLE_ADMIN")
	public Page<User> getAllUsers(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "created_at") String sortBy
			) {
		  Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.findAll(pageable);
	}
	
	@GetMapping("/users/candidates")
	public Page<User> getAllCandidates(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "created_at") String sortBy
			) {
		  Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.findAllCandidates(pageable);
	}

	@GetMapping("/users/employers")
	public Page<User> getAllEmployers(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "created_at") String sortBy
			) {
		  Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		return userRepository.findAllEmployers(pageable);
	}
}
