package com.datn.onlinejobportal.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.datn.onlinejobportal.exception.ResourceNotFoundException;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	UserRepository userRepository;
	 public User getUserByEmail(String email) throws ResourceNotFoundException {
			Optional<User> user = userRepository.findByEmail(email);

			if (user.isPresent()) {
				return user.get();
			} else {
				throw new ResourceNotFoundException("User", "email", email);
			}
		}
	
}