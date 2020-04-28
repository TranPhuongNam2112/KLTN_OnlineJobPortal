package com.datn.onlinejobportal.payload;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import com.datn.onlinejobportal.model.Role;

public class UserProfile {
    private Long id;
    private String name;
    private LocalDate joinedAt;
    private Set<Role> role;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Set<Role> getRole() {
		return role;
	}
	public void setRole(Set<Role> role) {
		this.role = role;
	}
	public UserProfile(Long id, String name, LocalDate joinedAt, Set<Role> role) {
		super();
		this.id = id;
		this.name = name;
		this.joinedAt = joinedAt;
		this.role = role;
	}


   
	
    
    
}