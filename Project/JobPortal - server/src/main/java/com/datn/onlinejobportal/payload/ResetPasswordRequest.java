package com.datn.onlinejobportal.payload;

import javax.validation.constraints.NotBlank;

import com.datn.onlinejobportal.constraint.PasswordMatches;


@PasswordMatches
public class ResetPasswordRequest {
	
	@NotBlank
	private String newpassword;
	
	@NotBlank
	private String reenterednewpassword;

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getReenterednewpassword() {
		return reenterednewpassword;
	}

	public void setReenterednewpassword(String reenterednewpassword) {
		this.reenterednewpassword = reenterednewpassword;
	}
	
	

}
