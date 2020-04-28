package com.datn.onlinejobportal.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.exception.BadRequestException;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.payload.SavedCandidateResponse;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.SavedCandidateRepository;
import com.datn.onlinejobportal.repository.SavedJobPostRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.util.AppConstants;

@Service
public class JobPostService {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SavedCandidateRepository savedCandidateRepository;
	
	@Autowired
	private SavedJobPostRepository savedJobPostRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	public JobPost createJobPost(JobPostRequest jobPostRequest) {
		JobPost jobpost = new JobPost();
		JobLocation joblocation = new JobLocation();
		joblocation.setStreet_address(jobPostRequest.getStreet_address());
		joblocation.setCity_province(jobPostRequest.getCity_province());
		joblocation.setJobpost(jobpost);
		jobpost.setJob_title(jobPostRequest.getJobtitle());
		jobpost.setJobtype(jobPostRequest.getJobType());
		jobpost.setJoblocation(joblocation);
		jobpost.setIndustry(jobPostRequest.getIndustry());
		jobpost.setJob_description(jobPostRequest.getJobdescription());
		jobpost.setCreatedAt(LocalDate.now());
		jobpost.setExpirationDate(jobPostRequest.getExpiredDate());

		return jobPostRepository.save(jobpost);
	}
	
	public JobPost getJobPostById(Long jobpostId, UserPrincipal currentUser) {
        JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(
                () -> new ResourceNotFoundException("Job Post", "id", jobpostId));
	
        return jobpost;
	}
	

	private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}
