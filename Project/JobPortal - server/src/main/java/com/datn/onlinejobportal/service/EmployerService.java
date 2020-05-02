package com.datn.onlinejobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.dto.CandidateSummary;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.security.UserPrincipal;


@Service
public class EmployerService {
	
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	
	public JobPost createJobPost(JobPostRequest jobPostRequest) {
		JobPost jobpost = new JobPost();
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setIndustry(jobPostRequest.getIndustry());
		jobpost.setMin_salary(jobPostRequest.getMinSalary());
		jobpost.setMax_salary(jobPostRequest.getMaxSalary());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());
		
		return jobPostRepository.save(jobpost);
	}
	
	public JobPost updateJobPost(Long jobpostid, JobPostRequest jobPostRequest) {
		JobPost jobpost = jobPostRepository.findById(jobpostid)
				.orElseThrow(() -> new ResourceNotFoundException("Job post", "id", jobpostid));
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setIndustry(jobPostRequest.getIndustry());
		jobpost.setMin_salary(jobPostRequest.getMinSalary());
		jobpost.setMax_salary(jobPostRequest.getMaxSalary());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());
		
		return jobPostRepository.save(jobpost);
	}
	
	
}