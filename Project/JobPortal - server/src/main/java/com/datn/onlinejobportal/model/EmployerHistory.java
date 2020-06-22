package com.datn.onlinejobportal.model;

import java.time.LocalDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="employer_history")
public class EmployerHistory {
	
	@EmbeddedId
	private EmployerHistoryId employerHistoryId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("candidateId")
	private Candidate candidate;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("employerId")
	private Employer employer;
	
	private LocalDate viewDate;

	public EmployerHistory() {
		super();
	}

	public EmployerHistory(Candidate candidate, Employer employer, LocalDate viewDate) {
		super();
		this.candidate = candidate;
		this.employer = employer;
		this.viewDate = viewDate;
		this.employerHistoryId = new EmployerHistoryId(candidate.getId(), employer.getId());
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public LocalDate getViewDate() {
		return viewDate;
	}

	public void setViewDate(LocalDate viewDate) {
		this.viewDate = viewDate;
	}
	
	

}
