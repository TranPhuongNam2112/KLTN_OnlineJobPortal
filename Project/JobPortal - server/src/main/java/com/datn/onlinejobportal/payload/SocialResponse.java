package com.datn.onlinejobportal.payload;

import java.util.Set;

import com.datn.onlinejobportal.model.Role;

public class SocialResponse {
	
	private Set<Role> roles;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SocialResponse( Set<Role> roles, String name) {
	
		this.roles = roles;
		this.name= name;
	}

	public Set<Role> getRoles() {
		return roles;
	}
}