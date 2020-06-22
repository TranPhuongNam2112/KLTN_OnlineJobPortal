package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.exception.BadRequestException;
import com.datn.onlinejobportal.model.AuthProvider;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.ConfirmationToken;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.PasswordResetToken;
import com.datn.onlinejobportal.model.Role;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.CandidateSignUpRequest;
import com.datn.onlinejobportal.payload.EmailPasswordResetRequest;
import com.datn.onlinejobportal.payload.EmployerSignUpRequest;
import com.datn.onlinejobportal.payload.JwtResponse;
import com.datn.onlinejobportal.payload.LoginRequest;
import com.datn.onlinejobportal.payload.ResetPasswordRequest;
import com.datn.onlinejobportal.payload.SignUpRequest;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.ConfirmationTokenRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.JobPostRepository;
import com.datn.onlinejobportal.repository.PasswordResetTokenRepository;
import com.datn.onlinejobportal.repository.RoleRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.TokenProvider;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.service.EmailService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private PasswordResetTokenRepository tokenRepository;
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private EmployerRepository employerRepository;

	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private JobPostRepository jobPostRepository;


	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginRequest.getEmail(),
						loginRequest.getPassword()
						)
				);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = tokenProvider.createToken(authentication);
		
		UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		/*
		
		if (!userRepository.findByEmail(userDetails.getEmail()).getEmailVerified()) {
			throw new BadRequestException("Email của người dùng chưa được xác thực!");
		}
*/
		
		return ResponseEntity.ok(new JwtResponse(token, 
												 userDetails.getId(), 
												 userDetails.getFullname(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@PostMapping("/signupforemployer")
	public ResponseEntity<?> registerEmployer(@Valid @RequestBody EmployerSignUpRequest employersignUpRequest) {
		if(userRepository.existsByEmail(employersignUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(employersignUpRequest.getName());
		user.setEmail(employersignUpRequest.getEmail());
		user.setPassword(employersignUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(employerRole);
		user.setRoles(roles);
		user.setCreatedAt(LocalDate.now());
		User result = userRepository.save(user);
		
		employerRepository.deleteDuplicateEmployer(employersignUpRequest.getCompanyname());
		jobPostRepository.deleteJobPostsByEmployer(employersignUpRequest.getCompanyname());

		Employer company = new Employer();
		company.setCompanyname(employersignUpRequest.getCompanyname());
		company.setPhone_number(employersignUpRequest.getPhonenumber());
		company.setIndustry(employersignUpRequest.getIndustry());
		company.setUser(user);
		employerRepository.save(company);


		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}

	@PostMapping("/signupforcandidate")
	public ResponseEntity<?> registerCandidate(@Valid @RequestBody CandidateSignUpRequest candidatesignUpRequest, HttpServletRequest request) {
		if(userRepository.existsByEmail(candidatesignUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(candidatesignUpRequest.getName());
		user.setEmail(candidatesignUpRequest.getEmail());
		user.setPassword(candidatesignUpRequest.getPassword());
		user.setProvider(AuthProvider.local);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Set<Role> roles = new HashSet<>();
		Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(candidateRole);
		user.setRoles(roles);
		user.setCreatedAt(LocalDate.now());

		user.setEmailVerified(false);
		User result = userRepository.save(user);


		// Generate random 36-character string token for confirmation link
		ConfirmationToken token = new ConfirmationToken();
		token.setConfirmationToken(UUID.randomUUID().toString());
        token.setUser(user);
        confirmationTokenRepository.save(token);

		String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";

		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
				+ appUrl + "/auth/confirm?token=" + token.getConfirmationToken());
		registrationEmail.setFrom("no-reply@memorynotfound.com");

		emailService.sendEmail(registrationEmail);


		Candidate candidate = new Candidate();
		candidate.setUser(user);
		candidateRepository.save(candidate);

		

		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/candidate/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}
	
	@GetMapping("/confirm")
	public ResponseEntity<Object> processConfirmationPage(@RequestParam("token") String token)
			throws URISyntaxException {
		User user = userRepository.findByConfirmationToken(token);
		URI notfoundURL = new URI("http://localhost:4200/notfound");
		URI verifyURL = new URI("http://localhost:4200/guest/validate");
		HttpHeaders httpHeaders = new HttpHeaders();

		if (user == null) {
			httpHeaders.setLocation(notfoundURL);
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		} else {
			user.setEmailVerified(true);
			userRepository.save(user);
			httpHeaders.setLocation(verifyURL);
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
	}


	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
		if(userRepository.existsByEmail(signUpRequest.getEmail())) {
			throw new BadRequestException("Email address already in use.");
		}

		// Creating user's account
		User user = new User();
		user.setName(signUpRequest.getName());
		user.setEmail(signUpRequest.getEmail());
		user.setPassword(signUpRequest.getPassword());
		user.setProvider(AuthProvider.local);

		user.setPassword(passwordEncoder.encode(user.getPassword()));

		Set<String> strRoles = signUpRequest.getRoles();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(candidateRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "employer":
					Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(employerRole);
					//Employer employer = new Employer()


					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		User result = userRepository.save(user);



		URI location = ServletUriComponentsBuilder
				.fromCurrentContextPath().path("/user/me")
				.buildAndExpand(result.getId()).toUri();

		return ResponseEntity.created(location)
				.body(new ApiResponse(true, "User registered successfully!"));
	}
	
	@PostMapping("/forgotpassword")
	public ResponseEntity<?> resetUserPassword(@Valid @RequestBody EmailPasswordResetRequest emailPasswordResetRequest, HttpServletRequest request) {
		String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";
		String redirectUrl = request.getScheme() + "://" + request.getServerName() + ":4200";
		User user = userRepository.findByEmail(emailPasswordResetRequest.getEmail());

		if (user == null)
		{
			return ResponseEntity.ok("We couldn't find your account with the email you have typed in!");
		}
		
		PasswordResetToken token = new PasswordResetToken();
		token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiryDate(30);
        tokenRepository.save(token);
		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(emailPasswordResetRequest.getEmail());
		registrationEmail.setSubject("Password reset request");
		registrationEmail.setText("To reset yourpassword, please click the link below:\n"
				+ redirectUrl + "/resetpassword?reset_token=" + token.getToken()
				
				);
		registrationEmail.setFrom("no-reply@memorynotfound.com");

		emailService.sendEmail(registrationEmail);
		
		return ResponseEntity.ok("Please check your email for further instructions.");
	}
	
	@PostMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest resetPasswordRequest)
	{
		if (!resetPasswordRequest.getNewpassword().equals(resetPasswordRequest.getReenterednewpassword()))
		{
			return ResponseEntity.ok(new BadRequestException("Bad!"));
		}
		else {
			PasswordResetToken usertoken = tokenRepository.findByToken(token);
	        User user = usertoken.getUser();
	        String updatedPassword = passwordEncoder.encode(resetPasswordRequest.getNewpassword());
	        userRepository.updatePassword(updatedPassword, user.getId());
	        tokenRepository.delete(usertoken);
	        return ResponseEntity.ok("Password updated successfully!");
		}
	}
	
	

}
