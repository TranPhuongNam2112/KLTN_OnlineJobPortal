package com.datn.onlinejobportal.controller;


import java.util.List;
import java.util.Locale;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.dto.UserProfile;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.ActiveUserStore;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	ActiveUserStore activeUserStore;

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


	@GetMapping("/loggedUsers")
	public List<String> getLoggedUsers(Locale locale, Model model) {
		return activeUserStore.getUsers();
	}

}
