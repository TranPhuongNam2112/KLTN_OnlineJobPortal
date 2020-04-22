package com.datn.onlinejobportal.model;

import java.io.Serializable;

import javax.persistence.Column;

public class UserFileId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6317919306523642255L;

	@Column(name="file_id")
	private String fileId;
	
	@Column(name="user_id")
	private Long userId;
}
