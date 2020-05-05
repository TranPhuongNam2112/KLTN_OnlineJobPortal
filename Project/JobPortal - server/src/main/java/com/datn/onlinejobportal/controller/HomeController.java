package com.datn.onlinejobportal.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.datn.onlinejobportal.dao.IJobPostDAO;
import com.datn.onlinejobportal.dao.SearchCriteria;
import com.datn.onlinejobportal.dto.JobPostSummary;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.security.CurrentUser;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.util.AppConstants;

@RestController
@RequestMapping("/")
public class HomeController {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
    private IJobPostDAO api;
	
	
	
	@GetMapping
	public Page<JobPostSummary> getJobPostsByJobType(@RequestParam("jobtype") String jobtype, @CurrentUser UserPrincipal currentUser, @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNo,
			@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE)int pageSize,
			@RequestParam(defaultValue = "expirationDate") String sortBy) {
		Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
		return jobPostRepository.getJobPostsByJobType(jobtype, pageable);
	}
	

 
    @RequestMapping(method = RequestMethod.GET, value = "/jobposts")
    @ResponseBody
    public List<JobPost> findAll(@RequestParam(value = "search", required = false) String search) {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1), 
                  matcher.group(2), matcher.group(3)));
            }
        }
        return api.searchJobPost(params);
    }
	
	
	
}
