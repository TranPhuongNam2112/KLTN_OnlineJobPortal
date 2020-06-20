package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class EmployerHistoryId implements Serializable {
	
	@Column(name="candidate_id")
	private Long candidateId;
	
	@Column(name="employer_id")
	private Long jobpostId;

	public EmployerHistoryId() {
		super();
	}

	public EmployerHistoryId(Long candidateId, Long jobpostId) {
		super();
		this.candidateId = candidateId;
		this.jobpostId = jobpostId;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public Long getJobpostId() {
		return jobpostId;
	}

	public void setJobpostId(Long jobpostId) {
		this.jobpostId = jobpostId;
	}
	

}
