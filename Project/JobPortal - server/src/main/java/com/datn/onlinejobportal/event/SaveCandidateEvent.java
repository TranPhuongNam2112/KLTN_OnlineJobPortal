package com.datn.onlinejobportal.event;

import org.springframework.context.ApplicationEvent;

@SuppressWarnings("serial")
public class SaveCandidateEvent extends ApplicationEvent {

	public Long employerId;
	
	public SaveCandidateEvent(Object source, Long employerId) {
		super(source);
		this.employerId = employerId;
	}

	public Long getEmployerId() {
		return employerId;
	}

	public void setEmployerId(Long employerId) {
		this.employerId = employerId;
	}
	
	

}
