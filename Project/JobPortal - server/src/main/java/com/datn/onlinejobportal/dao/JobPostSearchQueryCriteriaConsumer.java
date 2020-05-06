package com.datn.onlinejobportal.dao;

import java.util.function.Consumer;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.datn.onlinejobportal.util.SearchCriteria;


public class JobPostSearchQueryCriteriaConsumer implements Consumer<SearchCriteria>{

	private Predicate predicate;
	private CriteriaBuilder builder;
	@SuppressWarnings("rawtypes")
	private Root r;

	@SuppressWarnings("unchecked")
	@Override
	public void accept(SearchCriteria param) {
		if (param.getOperation().equalsIgnoreCase(">")) {
			predicate = builder.and(predicate, builder
					.greaterThanOrEqualTo(r.get(param.getKey()), param.getValue().toString()));
		} else if (param.getOperation().equalsIgnoreCase("<")) {
			predicate = builder.and(predicate, builder.lessThanOrEqualTo(
					r.get(param.getKey()), param.getValue().toString()));
		} else if (param.getOperation().equalsIgnoreCase(":")) {
			if (r.get(param.getKey()).getJavaType() == String.class) {
				predicate = builder.and(predicate, builder.like(
						r.get(param.getKey()), "%" + param.getValue() + "%"));
			} else {
				predicate = builder.and(predicate, builder.equal(
						r.get(param.getKey()), param.getValue()));
			}
		}
	}

	public JobPostSearchQueryCriteriaConsumer(Predicate predicate, CriteriaBuilder builder, @SuppressWarnings("rawtypes") Root r) {
		super();
		this.predicate = predicate;
		this.builder = builder;
		this.r = r;
	}

	public Predicate getPredicate() {
		return predicate;
	}

	public void setPredicate(Predicate predicate) {
		this.predicate = predicate;
	}

	public CriteriaBuilder getBuilder() {
		return builder;
	}

	public void setBuilder(CriteriaBuilder builder) {
		this.builder = builder;
	}

	@SuppressWarnings("rawtypes")
	public Root getR() {
		return r;
	}

	public void setR(@SuppressWarnings("rawtypes") Root r) {
		this.r = r;
	}
	
	
}
