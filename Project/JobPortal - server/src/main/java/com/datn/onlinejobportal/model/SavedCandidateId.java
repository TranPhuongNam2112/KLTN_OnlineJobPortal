package com.datn.onlinejobportal.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SavedCandidateId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6151936670059586698L;

	@Column(name="candidate_id")
	private Long candidateId;

	@Column(name="employer_id")
	private Long employerId;
	
	

	public SavedCandidateId() {
		super();
	}

	public SavedCandidateId(Long candidateId, Long employerId) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass())
			return false;

		SavedCandidateId that = (SavedCandidateId) o;
		return Objects.equals(candidateId, that.candidateId) &&
				Objects.equals(employerId, that.employerId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(candidateId, employerId);
	}
}
