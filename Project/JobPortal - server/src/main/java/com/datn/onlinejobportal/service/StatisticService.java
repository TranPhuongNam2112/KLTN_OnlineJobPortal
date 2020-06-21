package com.datn.onlinejobportal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.repository.JobPostRepository;

@Service
public class StatisticService {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	

}
