package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.JobPost;

public interface JobPostRepository extends JpaRepository<JobPost, Long> {

}
