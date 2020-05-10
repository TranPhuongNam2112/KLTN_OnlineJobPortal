package com.datn.onlinejobportal.model;



import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.datn.onlinejobportal.model.audit.DateAudit;

@SuppressWarnings("serial")
@Entity
@Table(name = "files")
public class DBFile extends DateAudit {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	private String fileName;

	private String fileType;

	@Lob
	private byte[] data;
	
	
    @OneToOne(mappedBy = "files")
    private Candidate candidate;
    
    @OneToOne(mappedBy = "files")
    private User user;

	public DBFile() {

	}

	public DBFile(String fileName, String fileType, byte[] data) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}


}