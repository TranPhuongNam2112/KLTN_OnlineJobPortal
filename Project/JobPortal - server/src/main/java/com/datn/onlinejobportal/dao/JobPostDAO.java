package com.datn.onlinejobportal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import com.datn.onlinejobportal.util.*;

import com.datn.onlinejobportal.model.JobPost;

@Repository
public class JobPostDAO implements IJobPostDAO{

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<JobPost> searchJobPost(List<SearchCriteria> params) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<JobPost> query = builder.createQuery(JobPost.class);
		@SuppressWarnings("rawtypes")
		Root r = query.from(JobPost.class);
		

		Predicate predicate = builder.conjunction();

		JobPostSearchQueryCriteriaConsumer searchConsumer = 
				new JobPostSearchQueryCriteriaConsumer(predicate, builder, r);
		params.stream().forEach(searchConsumer);
		predicate = searchConsumer.getPredicate();
		query.where(predicate);

		List<JobPost> result = entityManager.createQuery(query).getResultList();
		return result;
	}

	@Override
	public void save(JobPost entity) {
		entityManager.persist(entity);
	}

}
