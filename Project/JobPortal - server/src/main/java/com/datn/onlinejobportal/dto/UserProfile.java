package com.datn.onlinejobportal.dto;

import java.time.LocalDate;

public class UserProfile {
	
	private String name;
	private byte[] image;
	private LocalDate joinedAt;
	
	

	public UserProfile(String name, byte[] image, LocalDate joinedAt) {
		super();
		this.name = name;
		this.image = image;
		this.joinedAt = joinedAt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getJoinedAt() {
		return joinedAt;
	}
	public void setJoinedAt(LocalDate joinedAt) {
		this.joinedAt = joinedAt;
	}
}
