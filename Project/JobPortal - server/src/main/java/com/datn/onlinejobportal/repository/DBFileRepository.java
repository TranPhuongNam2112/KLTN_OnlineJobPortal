package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.DBFile;

public interface DBFileRepository extends JpaRepository<DBFile, String> {

}
