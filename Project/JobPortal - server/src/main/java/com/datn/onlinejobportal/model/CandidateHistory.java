package com.datn.onlinejobportal.model;

import java.time.LocalDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="candidate_history")
public class CandidateHistory {
	
	@EmbeddedId
	private CandidateHistoryId candidateHistoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("jobpostId")
	private JobPost jobpost;
	
	private LocalDate viewDate;
	

	public CandidateHistory() {
		super();
	}
	
	

	public CandidateHistory(Candidate candidate, JobPost jobpost, LocalDate viewDate) {
		super();
		this.candidate = candidate;
		this.jobpost = jobpost;
		this.viewDate = viewDate;
		this.candidateHistoryId = new CandidateHistoryId(candidate.getId(), jobpost.getId());
	}



	public CandidateHistoryId getCandidateHistoryId() {
		return candidateHistoryId;
	}

	public void setCandidateHistoryId(CandidateHistoryId candidateHistoryId) {
		this.candidateHistoryId = candidateHistoryId;
	}


	public LocalDate getViewDate() {
		return viewDate;
	}



	public void setViewDate(LocalDate viewDate) {
		this.viewDate = viewDate;
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
	
	
}
