package com.datn.onlinejobportal.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.datn.onlinejobportal.exception.BadRequestException;
import com.datn.onlinejobportal.exception.OAuth2AuthenticationProcessingException;
import com.datn.onlinejobportal.model.AuthProvider;
import com.datn.onlinejobportal.model.Candidate;
import com.datn.onlinejobportal.model.ERole;
import com.datn.onlinejobportal.model.Employer;
import com.datn.onlinejobportal.model.Role;
import com.datn.onlinejobportal.model.User;
import com.datn.onlinejobportal.payload.ApiResponse;
import com.datn.onlinejobportal.payload.AuthResponse;
import com.datn.onlinejobportal.payload.CandidateSignUpRequest;
import com.datn.onlinejobportal.payload.EmployerSignUpRequest;
import com.datn.onlinejobportal.payload.JwtResponse;
import com.datn.onlinejobportal.payload.LoginRequest;
import com.datn.onlinejobportal.payload.SignUpRequest;
import com.datn.onlinejobportal.payload.SocialResponse;
import com.datn.onlinejobportal.repository.CandidateRepository;
import com.datn.onlinejobportal.repository.EmployerRepository;
import com.datn.onlinejobportal.repository.RoleRepository;
import com.datn.onlinejobportal.repository.UserRepository;
import com.datn.onlinejobportal.security.CustomUserDetailsService;
import com.datn.onlinejobportal.security.TokenProvider;
import com.datn.onlinejobportal.security.UserPrincipal;
import com.datn.onlinejobportal.security.oauth2.user.OAuth2UserInfo;
import com.datn.onlinejobportal.service.EmailService;
import com.datn.onlinejobportal.service.UserService;

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
	private EmployerRepository employerRepository;


	@Autowired
	private CandidateRepository candidateRepository;
	
	@Autowired
	private EmailService emailService;

	@Autowired
	private UserService userService;

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

		
		return ResponseEntity.ok(new JwtResponse(token, 
												 userDetails.getId(), 
												 userDetails.getFullname(), 
												 userDetails.getEmail(), 
												 roles));
	}
	@PostMapping("/loginGGForCandidate")
	public ResponseEntity<?> loginGGForCandidate(@Valid @RequestBody OAuth2UserInfo oAuth2UserInfo,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
			User user = userService.getUserByEmail(oAuth2UserInfo.getEmail());
			Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new BadRequestException("Error: Role is not found."));
			if (user.getRoles().contains(candidateRole)) {
				user.setName(oAuth2UserInfo.getName());
				user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
				user.setUpdatedAt(Instant.now());
				User result = userRepository.save(user);
				return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));

			} else
				throw new BadRequestException("Look like you are loging in as employer user");
		}
		// Creating user's account
		User user = new User();
		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setProviderId(oAuth2UserInfo.getId());
		user.setProvider(AuthProvider.google);
		user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
		Set<Role> roles = new HashSet<>();
		Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(candidateRole);
		user.setRoles(roles);
		user.setCreatedAt(Instant.now());
		user.setEmailVerified(true);
		user.setConfirmationToken(UUID.randomUUID().toString());
		User result = userRepository.save(user);
		Candidate candidate = new Candidate();
		candidate.setUser(user);
		candidateRepository.save(candidate);
		return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
	}

	@PostMapping("/loginGGForEmployer")
	public ResponseEntity<?> loginGGForEmployer(@Valid @RequestBody OAuth2UserInfo oAuth2UserInfo,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
			User user = userService.getUserByEmail(oAuth2UserInfo.getEmail());
			;
			Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
					.orElseThrow(() -> new BadRequestException("Error: Role is not found."));
			if (user.getRoles().contains(employerRole)) {
				user.setName(oAuth2UserInfo.getName());
				user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
				user.setUpdatedAt(Instant.now());
				User result = userRepository.save(user);
				return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
			} else
				throw new BadRequestException("Look like you are loging in as candidate user");
		}
		// Creating user's account
		User user = new User();
		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setProviderId(oAuth2UserInfo.getId());
		user.setProvider(AuthProvider.google);
		user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
		Set<Role> roles = new HashSet<>();
		Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(employerRole);
		user.setRoles(roles);
		user.setCreatedAt(Instant.now());
		user.setEmailVerified(true);
		user.setConfirmationToken(UUID.randomUUID().toString());
		User result = userRepository.save(user);
		Employer employer = new Employer();
		employer.setUser(user);
		employerRepository.save(employer);
		return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));

	}

	@PostMapping("/loginFBForCandidate")
	public ResponseEntity<?> loginFBForCandidate(@Valid @RequestBody OAuth2UserInfo oAuth2UserInfo,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
			User user = userService.getUserByEmail(oAuth2UserInfo.getEmail());
			;
			Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
					.orElseThrow(() -> new BadRequestException("Error: Role is not found."));
			if (user.getRoles().contains(candidateRole)) {
				user.setName(oAuth2UserInfo.getName());
				user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
				user.setUpdatedAt(Instant.now());
				User result = userRepository.save(user);
				return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
			} else
				throw new BadRequestException("Look like you are loging in as employer user");
		}
		User user = new User();
		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setProviderId(oAuth2UserInfo.getId());
		user.setProvider(AuthProvider.facebook);
		user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
		Set<Role> roles = new HashSet<>();
		Role candidateRole = roleRepository.findByName(ERole.ROLE_CANDIDATE)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(candidateRole);
		user.setRoles(roles);
		user.setCreatedAt(Instant.now());
		user.setEmailVerified(true);
		user.setConfirmationToken(UUID.randomUUID().toString());
		User result = userRepository.save(user);
		Candidate candidate = new Candidate();
		candidate.setUser(user);
		candidateRepository.save(candidate);
		return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
	}

	@PostMapping("/loginFBForEmployer")
	public ResponseEntity<?> loginFBForEmployer(@Valid @RequestBody OAuth2UserInfo oAuth2UserInfo,
			HttpServletRequest request) {
		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}
		if (userRepository.existsByEmail(oAuth2UserInfo.getEmail())) {
			User user = userService.getUserByEmail(oAuth2UserInfo.getEmail());
			;
			Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
					.orElseThrow(() -> new BadRequestException("Error: Role is not found."));
			if (user.getRoles().contains(employerRole)) {
				user.setName(oAuth2UserInfo.getName());
				user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
				user.setUpdatedAt(Instant.now());
				User result = userRepository.save(user);
				return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
			} else
				throw new BadRequestException("Look like you are loging in as candidate user");
		}
		User user = new User();
		user.setName(oAuth2UserInfo.getName());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setProviderId(oAuth2UserInfo.getId());
		user.setProvider(AuthProvider.facebook);
		user.setImageUrl(oAuth2UserInfo.getPhotoUrl());
		Set<Role> roles = new HashSet<>();
		Role employerRole = roleRepository.findByName(ERole.ROLE_EMPLOYER)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(employerRole);
		user.setRoles(roles);
		user.setCreatedAt(Instant.now());
		user.setEmailVerified(true);
		user.setConfirmationToken(UUID.randomUUID().toString());
		User result = userRepository.save(user);
		Employer employer = new Employer();
		employer.setUser(user);
		employerRepository.save(employer);
		return ResponseEntity.ok(new SocialResponse(user.getRoles(), user.getName()));
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
		user.setCreatedAt(Instant.now());
		User result = userRepository.save(user);

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
		user.setCreatedAt(Instant.now());

		user.setEmailVerified(false);

		// Generate random 36-character string token for confirmation link
		user.setConfirmationToken(UUID.randomUUID().toString());

		User result = userRepository.save(user);

		String appUrl = request.getScheme() + "://" + request.getServerName() + ":8080";

		SimpleMailMessage registrationEmail = new SimpleMailMessage();
		registrationEmail.setTo(user.getEmail());
		registrationEmail.setSubject("Registration Confirmation");
		registrationEmail.setText("To confirm your e-mail address, please click the link below:\n"
				+ appUrl + "/auth/confirm?token=" + user.getConfirmationToken());
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

}
