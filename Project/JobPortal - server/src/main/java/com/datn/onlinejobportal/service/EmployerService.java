package com.datn.onlinejobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.repository.IndustryRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;


@Service
public class EmployerService {
	
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private IndustryRepository industryRepository;

	
	
	public JobPost createJobPost(JobPostRequest jobPostRequest) {
		JobPost jobpost = new JobPost();
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setIndustries(industryRepository.getAllIndustriesByNames(jobPostRequest.getIndustry()));
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
		jobpost.setIndustries(industryRepository.getAllIndustriesByNames(jobPostRequest.getIndustry()));
		jobpost.setMin_salary(jobPostRequest.getMinSalary());
		jobpost.setMax_salary(jobPostRequest.getMaxSalary());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());
		
		return jobPostRepository.save(jobpost);
	}
	
	
}
