package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SavedCandidateId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6151936670059586698L;

	@Column(name="candidate_id")
	private Long candidateId;
	
	@Column(name="employer_id")
	private Long employerId;
}
