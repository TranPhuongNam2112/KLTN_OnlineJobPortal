package com.datn.onlinejobportal.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SavedJobPostId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8959434550572882948L;

	@Column(name="candidate_id")
	private Long candidateId;
	
	@Column(name="job_post_id")
	private Long jobpostId;
	
	
	
	public SavedJobPostId() {
		super();
	}

	public SavedJobPostId(Long candidateId, Long jobpostId) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass())
			return false;

		SavedJobPostId that = (SavedJobPostId) o;
		return Objects.equals(candidateId, that.candidateId) &&
				Objects.equals(jobpostId, that.jobpostId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(candidateId, jobpostId);
	}
	
}
