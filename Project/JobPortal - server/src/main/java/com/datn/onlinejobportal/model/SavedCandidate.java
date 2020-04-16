package com.datn.onlinejobportal.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import com.datn.onlinejobportal.model.audit.DateAudit;


@Entity
@Table(name="saved_candidate")
public class SavedCandidate extends DateAudit {
	
	@EmbeddedId
	private SavedCandidateId savedCandidateId;

	@ManyToOne
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne
	@MapsId("employerId")
	private Employer employer;


	public SavedCandidate() {

	}

	public SavedCandidate(Employer employer, Candidate candidate) {
		this.employer = employer;
		this.candidate = candidate;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

}