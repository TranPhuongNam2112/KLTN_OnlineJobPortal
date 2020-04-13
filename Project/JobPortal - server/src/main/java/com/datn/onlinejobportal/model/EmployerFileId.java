package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;

public class EmployerFileId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6317919306523642255L;

	@Column(name="file_id")
	private String fileId;
	
	@Column(name="employer_id")
	private Long employerId;
}