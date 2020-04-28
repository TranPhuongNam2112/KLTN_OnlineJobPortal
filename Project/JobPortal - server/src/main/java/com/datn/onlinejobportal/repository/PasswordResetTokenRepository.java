package com.datn.onlinejobportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.onlinejobportal.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	PasswordResetToken findByToken(String token);
}
