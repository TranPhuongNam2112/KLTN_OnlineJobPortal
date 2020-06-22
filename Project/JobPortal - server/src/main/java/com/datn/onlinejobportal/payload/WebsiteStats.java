package com.datn.onlinejobportal.payload;

public class WebsiteStats {
	
	private Long currentUsersOnline;
	
	private Long countRegisteredEmployers;
	
	private Long countAllEmployers;
	
	private Long countAllCandidates;
	
	private Long countNewCandidatesToday;
	
	private Long countNewRegisteredEmployerToday;
	
	private Long countNewJobPostsToday;

	public WebsiteStats() {
		super();
	}

	public Long getCurrentUsersOnline() {
		return currentUsersOnline;
	}

	public void setCurrentUsersOnline(Long currentUsersOnline) {
		this.currentUsersOnline = currentUsersOnline;
	}

	public Long getCountRegisteredEmployers() {
		return countRegisteredEmployers;
	}

	public void setCountRegisteredEmployers(Long countRegisteredEmployers) {
		this.countRegisteredEmployers = countRegisteredEmployers;
	}

	public Long getCountAllEmployers() {
		return countAllEmployers;
	}

	public void setCountAllEmployers(Long countAllEmployers) {
		this.countAllEmployers = countAllEmployers;
	}

	public Long getCountAllCandidates() {
		return countAllCandidates;
	}

	public void setCountAllCandidates(Long countAllCandidates) {
		this.countAllCandidates = countAllCandidates;
	}

	public Long getCountNewCandidatesToday() {
		return countNewCandidatesToday;
	}

	public void setCountNewCandidatesToday(Long countNewCandidatesToday) {
		this.countNewCandidatesToday = countNewCandidatesToday;
	}

	public Long getCountNewRegisteredEmployerToday() {
		return countNewRegisteredEmployerToday;
	}

	public void setCountNewRegisteredEmployerToday(Long countNewRegisteredEmployerToday) {
		this.countNewRegisteredEmployerToday = countNewRegisteredEmployerToday;
	}

	public Long getCountNewJobPostsToday() {
		return countNewJobPostsToday;
	}

	public void setCountNewJobPostsToday(Long countNewJobPostsToday) {
		this.countNewJobPostsToday = countNewJobPostsToday;
	}
	

}
