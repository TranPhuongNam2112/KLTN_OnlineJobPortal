package com.datn.onlinejobportal.dao;

import java.util.List;

import com.datn.onlinejobportal.model.JobPost;


public interface IJobPostDAO {
	List<JobPost> searchJobPost(List<SearchCriteria> params);

    void save(JobPost entity);
}
