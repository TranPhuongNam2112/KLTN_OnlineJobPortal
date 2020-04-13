package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;

public class CandidateFileId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5837655072996849356L;

	@Column(name="file_id")
	private String fileId;
	
	@Column(name="candidate_id")
	private Long candidateId;
}