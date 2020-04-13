package com.datn.onlinejobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	public List<User> getAllUsers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		List<User> users = userRepository.findAll();
		return users;
	}
	
	@GetMapping("/users/candidates")
	public List<User> getAllCandidates(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		List<User> users = userRepository.findByRoles_Name(ERole.ROLE_CANDIDATE);
		return users;
	}

	@GetMapping("/users/employers")
	public List<User> getAllEmployers(@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		List<User> users = userRepository.findByRoles_Name(ERole.ROLE_EMPLOYER);
		return users;
	}

	@GetMapping("/users/{name}")
	public UserProfile getUserProfile(@PathVariable(value = "name") String name) {
		User user = userRepository.findByName(name)
				.orElseThrow(() -> new ResourceNotFoundException("User", "name", name));
		
		UserProfile userProfile = new UserProfile(user.getId(), user.getName(), user.getRoles());
		return userProfile;
	}

	@GetMapping("/users/{username}/")
	public PagedResponse<PollResponse> getPollsCreatedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsCreatedBy(username, currentUser, page, size);
	}


	@GetMapping("/users/{username}/votes")
	public PagedResponse<PollResponse> getPollsVotedBy(@PathVariable(value = "username") String username,
			@CurrentUser UserPrincipal currentUser,
			@RequestParam(value = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
			@RequestParam(value = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size) {
		return pollService.getPollsVotedBy(username, currentUser, page, size);
	}
}
