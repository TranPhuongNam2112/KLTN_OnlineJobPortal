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
	private Long employerId;

	public EmployerHistoryId() {
		super();
	}

	public EmployerHistoryId(Long candidateId, Long employerId) {
		super();
		this.candidateId = candidateId;
		this.employerId = employerId;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public Long getEmployerId() {
		return employerId;
	}

	public void setEmployerId(Long employerId) {
		this.employerId = employerId;
	}
	
	

}
