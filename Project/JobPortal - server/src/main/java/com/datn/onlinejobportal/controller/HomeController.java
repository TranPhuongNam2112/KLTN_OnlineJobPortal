package com.datn.onlinejobportal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	
}
