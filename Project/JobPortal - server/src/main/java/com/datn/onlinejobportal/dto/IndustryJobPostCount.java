package com.datn.onlinejobportal.dto;

public class IndustryJobPostCount {
	private Long id;
	private Long count;
	public IndustryJobPostCount(Long id, Long count) {
		super();
		this.id = id;
		this.count = count;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	
	
}
