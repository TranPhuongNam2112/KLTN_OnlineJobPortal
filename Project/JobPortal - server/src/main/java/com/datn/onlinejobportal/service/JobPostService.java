package com.datn.onlinejobportal.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.JobTypeRepository;
import com.datn.onlinejobportal.security.UserPrincipal;

@Service
public class JobPostService {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private JobTypeRepository jobTypeRepository;
	
	public JobPost createJobPost(JobPostRequest jobPostRequest) {
		JobPost jobpost = new JobPost();
		JobLocation joblocation = new JobLocation();
		joblocation.setStreet_address(jobPostRequest.getStreet_address());
		joblocation.setCity_province(jobPostRequest.getCity_province());
		joblocation.setJobpost(jobpost);
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJobtype(jobTypeRepository.findByJob_type_name(jobPostRequest.getJobType()));
		jobpost.setJoblocation(joblocation);
		jobpost.setIndustry(jobPostRequest.getIndustry());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setCreatedAt(LocalDate.now());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());
		jobpost.setRequiredexpreienceyears(jobPostRequest.getRequiredexperience());

		return jobPostRepository.save(jobpost);
	}
	
	public JobPost getJobPostById(Long jobpostId, UserPrincipal currentUser) {
        JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(
                () -> new ResourceNotFoundException("Job Post", "id", jobpostId));
	
        return jobpost;
	}
	
	
}
