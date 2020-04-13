package com.datn.onlinejobportal.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name="employer_file")
public class EmployerFile {
	
	@EmbeddedId
	private EmployerFileId employerFileId;

	@ManyToOne
	@MapsId("employerId")
	private Employer employer;

	@ManyToOne
	@MapsId("fileId")
	private DBFile files;

	@Column
	private Date added_date;

	public EmployerFile() {
		super();
	}

	public EmployerFile(Employer employer, DBFile files, Date added_date) {
		super();
		this.employer = employer;
		this.files = files;
		this.added_date = added_date;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public DBFile getFiles() {
		return files;
	}

	public void setFiles(DBFile files) {
		this.files = files;
	}

	public Date getAdded_date() {
		return added_date;
	}

	public void setAdded_date(Date added_date) {
		this.added_date = added_date;
	}
	
	
}
