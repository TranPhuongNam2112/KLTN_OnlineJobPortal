package com.datn.onlinejobportal.service;

public interface ISecurityUserServive {
	String validatePasswordResetToken(long id, String token);
}
