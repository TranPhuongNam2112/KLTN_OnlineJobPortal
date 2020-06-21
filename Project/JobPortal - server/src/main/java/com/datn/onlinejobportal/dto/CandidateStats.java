package com.datn.onlinejobportal.dto;

public class CandidateStats {
	
	private Long viewedEmployersCount;
	
	private Long savedEmployersCount;

	public CandidateStats() {
		super();
	}

	public CandidateStats(Long viewedEmployersCount, Long savedEmployersCount) {
		super();
		this.viewedEmployersCount = viewedEmployersCount;
		this.savedEmployersCount = savedEmployersCount;
	}

	public Long getViewedEmployersCount() {
		return viewedEmployersCount;
	}

	public void setViewedEmployersCount(Long viewedEmployersCount) {
		this.viewedEmployersCount = viewedEmployersCount;
	}

	public Long getSavedEmployersCount() {
		return savedEmployersCount;
	}

	public void setSavedEmployersCount(Long savedEmployersCount) {
		this.savedEmployersCount = savedEmployersCount;
	}

	
}
