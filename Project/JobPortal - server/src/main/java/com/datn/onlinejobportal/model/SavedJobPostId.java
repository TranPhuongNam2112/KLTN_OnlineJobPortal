package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SavedJobPostId implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8959434550572882948L;

	@Column(name="candidate_id")
	private Long candidateId;
	
	@Column(name="job_post_id")
	private Long jobpostId;
	
}
