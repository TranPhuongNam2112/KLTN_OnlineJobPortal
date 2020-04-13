package com.datn.onlinejobportal.payload;

import java.time.Instant;
import java.util.Set;

import com.datn.onlinejobportal.model.Role;

public class UserProfile {
    private Long id;
    private String name;
    private Set<Role> role;


    public UserProfile(Long id, String name, Set<Role> role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

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

	public Set<Role> getRole() {
		return role;
	}

	public void setRole(Set<Role> role) {
		this.role = role;
	}
    
    
}