package com.datn.onlinejobportal.dao;

import java.util.List;

import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.util.SearchCriteria;


public interface IJobPostDAO {
	List<JobPost> searchJobPost(List<SearchCriteria> params);

    void save(JobPost entity);
}
