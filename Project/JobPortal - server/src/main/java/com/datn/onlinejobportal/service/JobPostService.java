package com.datn.onlinejobportal.service;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.JobLocation;
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.SavedCandidate;
import com.datn.onlinejobportal.payload.JobLocationRequest;
import com.datn.onlinejobportal.payload.JobPostRequest;
import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.SavedCandidateRepository;
import com.datn.onlinejobportal.security.UserPrincipal;

@Service
public class JobPostService {
	
	@Autowired
	private JobPostRepository jobPostRepository;
	
	@Autowired
	private EmployerRepository employerRepository;
	
	@Autowired
	private SavedCandidateRepository savedCandidateRepository;
	
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
		jobpost.setCreatedAt(Instant.now());
		jobpost.setExpirationDateTime(jobPostRequest.getExpiredDate());

		return jobPostRepository.save(jobpost);
	}
	
	public JobPost getJobPostById(Long jobpostId, UserPrincipal currentUser) {
        JobPost jobpost = jobPostRepository.findById(jobpostId).orElseThrow(
                () -> new ResourceNotFoundException("Job Post", "id", jobpostId));
	
        return jobpost;
	}
	/*
	public PagedResponse<CandidateResponse> getAllSavedCandidates(UserPrincipal currentUser, int page, int size) {
        validatePageNumberAndSize(page, size);

        // Retrieve Polls
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
        Long employerId = employerRepository.getEmployerIdByAccount_Id(currentUser.getId());
        Page<Candidate> savedcandidates = savedCandidateRepository.findSavedCandidatesByEmployerId(employerId, pageable);

        if(savedcandidates.getNumberOfElements() == 0) {
            return new PagedResponse<>(Collections.emptyList(), savedcandidates.getNumber(),
            		savedcandidates.getSize(), savedcandidates.getTotalElements(), savedcandidates.getTotalPages(), savedcandidates.isLast());
        }

        // Map Polls to PollResponses containing vote counts and poll creator details
        List<Long> savedCandidatesIds = savedcandidates.map(Candidate::getId).getContent();
        Map<Long, String> nameMap = getChoiceVoteCountMap(pollIds);
        Map<Long, Date> DoBMap = getPollUserVoteMap(currentUser, pollIds);
        Map<Long, User> creatorMap = getPollCreatorMap(polls.getContent());

        List<PollResponse> pollResponses = polls.map(poll -> {
            return ModelMapper.mapPollToPollResponse(poll,
                    choiceVoteCountMap,
                    creatorMap.get(poll.getCreatedBy()),
                    pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
        }).getContent();

        return new PagedResponse<>(pollResponses, polls.getNumber(),
                polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
    }
	
	private Map<Long, String> getCandidteNameMap(List<Long> candidateIds) {
        // Retrieve Vote Counts of every Choice belonging to the given pollIds
        List<String> candidatesname = candidateRepository.getNameByCandidateId(candidateIds);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        return choiceVotesMap;
    }
    */

}
