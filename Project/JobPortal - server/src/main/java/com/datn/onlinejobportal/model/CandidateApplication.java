package com.datn.onlinejobportal.model;

import java.time.LocalDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="candidate_applications")
public class CandidateApplication {

	@EmbeddedId
	private CandidateApplicationId candidateApplicationId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("jobpostId")
	private JobPost jobpost;

	private LocalDate appliedDate;

	public CandidateApplication() {
		super();
	}

	public CandidateApplication(Candidate candidate, JobPost jobpost,
			LocalDate appliedDate) {
		this.candidateApplicationId = new CandidateApplicationId(candidate.getId(), jobpost.getId());
		this.candidate = candidate;
		this.jobpost = jobpost;
		this.appliedDate = appliedDate;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public JobPost getJobpost() {
		return jobpost;
	}

	public void setJobpost(JobPost jobpost) {
		this.jobpost = jobpost;
	}

	public LocalDate getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}




}
