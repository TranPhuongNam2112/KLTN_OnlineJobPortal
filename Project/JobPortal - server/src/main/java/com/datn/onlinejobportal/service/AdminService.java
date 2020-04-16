package com.datn.onlinejobportal.service;

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
import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.PagedResponse;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.util.AppConstants;

@Service
public class AdminService {
	/*
	@Autowired
	private UserRepository userRepository;

	public PagedResponse<User> getAllUsers(UserPrincipal currentUser, int page, int size) {
		validatePageNumberAndSize(page, size);

		// Retrieve Polls
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdAt");
		Page<User> users = userRepository.findAll(pageable);

		if(users.getNumberOfElements() == 0) {
			return new PagedResponse<>(Collections.emptyList(), users.getNumber(),
					users.getSize(), users.getTotalElements(), users.getTotalPages(), users.isLast());
		}

		// Map Polls to PollResponses containing vote counts and poll creator details
		List<Long> userIds = users.map(User::getId).getContent();
		Map<Long, Long> choiceVoteCountMap = getChoiceVoteCountMap(pollIds);
		Map<Long, Long> pollUserVoteMap = getPollUserVoteMap(currentUser, pollIds);
		Map<Long, User> creatorMap = getPollCreatorMap(users.getContent());

		List<User> pollResponses = users.map(poll -> {
			return ModelMapper.mapPollToPollResponse(poll,
					choiceVoteCountMap,
					creatorMap.get(poll.getCreatedBy()),
					pollUserVoteMap == null ? null : pollUserVoteMap.getOrDefault(poll.getId(), null));
		}).getContent();

		return new PagedResponse<>(pollResponses, polls.getNumber(),
				polls.getSize(), polls.getTotalElements(), polls.getTotalPages(), polls.isLast());
				
	}
	
    private Map<Long, Long> getChoiceVoteCountMap(List<Long> pollIds) {
        // Retrieve Vote Counts of every Choice belonging to the given pollIds
        List<ChoiceVoteCount> votes = voteRepository.countByPollIdInGroupByChoiceId(pollIds);

        Map<Long, Long> choiceVotesMap = votes.stream()
                .collect(Collectors.toMap(ChoiceVoteCount::getChoiceId, ChoiceVoteCount::getVoteCount));

        return choiceVotesMap;
    }

    private Map<Long, Long> getPollUserVoteMap(UserPrincipal currentUser, List<Long> postIds) {
        // Retrieve Votes done by the logged in user to the given pollIds
        Map<Long, Long> pollUserVoteMap = null;
        if(currentUser != null) {
            List<JobPost> userSavedJobPosts = jobPostRepository.findByUserIdAndPollIdIn(currentUser.getId(), postIds);

            pollUserVoteMap = userVotes.stream()
                    .collect(Collectors.toMap(vote -> vote.getPoll().getId(), vote -> vote.getChoice().getId()));
        }
        return pollUserVoteMap;
    }

    Map<Long, User> getPollCreatorMap(List<JobPost> jobposts) {
        // Get Poll Creator details of the given list of polls
        List<Long> creatorIds = jobposts.stream()
                .map(JobPost::getCreatedBy)
                .distinct()
                .collect(Collectors.toList());

        List<User> creators = userRepository.findByIdIn(creatorIds);
        Map<Long, User> creatorMap = creators.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        return creatorMap;
    }
    */

	private void validatePageNumberAndSize(int page, int size) {
        if(page < 0) {
            throw new BadRequestException("Page number cannot be less than zero.");
        }

        if(size > AppConstants.MAX_PAGE_SIZE) {
            throw new BadRequestException("Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
        }
    }
}

