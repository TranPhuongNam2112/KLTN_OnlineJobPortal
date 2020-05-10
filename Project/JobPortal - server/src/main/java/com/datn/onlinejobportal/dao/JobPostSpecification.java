package com.datn.onlinejobportal.dao;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.datn.onlinejobportal.model.JobPost;
import com.datn.onlinejobportal.util.SpecSearchCriteria;

@SuppressWarnings("serial")
public class JobPostSpecification implements Specification<JobPost> {
 
	private SpecSearchCriteria criteria;
    
    public JobPostSpecification(final SpecSearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}

	public SpecSearchCriteria getCriteria() {
		return criteria;
	}
 
    @Override
    public Predicate toPredicate(
      Root<JobPost> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
     
        switch (criteria.getOperation()) {
        case EQUALITY:
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        case NEGATION:
            return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
        case GREATER_THAN:
            return builder.greaterThan(root.<String> get(
              criteria.getKey()), criteria.getValue().toString());
        case LESS_THAN:
            return builder.lessThan(root.<String> get(
              criteria.getKey()), criteria.getValue().toString());
        case LIKE:
            return builder.like(root.<String> get(
              criteria.getKey()), criteria.getValue().toString());
        case STARTS_WITH:
            return builder.like(root.<String> get(criteria.getKey()), criteria.getValue() + "%");
        case ENDS_WITH:
            return builder.like(root.<String> get(criteria.getKey()), "%" + criteria.getValue());
        case CONTAINS:
            return builder.like(root.<String> get(
              criteria.getKey()), "%" + criteria.getValue() + "%");
        default:
            return null;
        }
    }
}
