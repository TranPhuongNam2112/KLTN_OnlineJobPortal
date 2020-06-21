package com.datn.onlinejobportal.dto;

public class JobPostRank {
	
	public Long id;
	
	public Long viewedCount;

	public JobPostRank() {
		super();
	}

	public JobPostRank(Long id, Long viewedCount) {
		super();
		this.id = id;
		this.viewedCount = viewedCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getViewedCount() {
		return viewedCount;
	}

	public void setViewedCount(Long viewedCount) {
		this.viewedCount = viewedCount;
	}
	
	

}
