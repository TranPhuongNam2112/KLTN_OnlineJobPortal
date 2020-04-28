package com.datn.onlinejobportal.service;

import java.util.Arrays;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.datn.onlinejobportal.model.PasswordResetToken;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.PasswordResetTokenRepository;
import com.datn.onlinejobportal.service.ISecurityUserServive;

@Service
public class ISecurityUserServiceImpl implements ISecurityUserServive {
	@Autowired
	private PasswordResetTokenRepository passwordTokenRepository;
	@Override
	public String validatePasswordResetToken(long id, String token) {
		final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);
		if ((passToken == null) || (passToken.getUser().getId() != id)) {
			return "invalidToken";
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return "expired";
		}
		final User user = passToken.getUser();
		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
		SecurityContextHolder.getContext().setAuthentication(auth);
		return null;
	}
}
