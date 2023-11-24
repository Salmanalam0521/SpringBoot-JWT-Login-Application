package com.incresol.app.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.incresol.app.entities.User;
import com.incresol.app.model.GenerateNewPassword;
import com.incresol.app.model.HttpStatusResponse;
import com.incresol.app.model.UserResponse;
import com.incresol.app.repository.UserRepository;
import com.incresol.app.security.JwtHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepo;

//	@Autowired
//	private HttpStatusResponse httpStatusResponse;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtHelper helper;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private HttpServletRequest request;

	public String getUserName() {
		String header = this.request.getHeader("Authorization");
		String token = header.substring(7);
		return this.helper.getUsernameFromToken(token);
	}

	public List<User> getUsers() {
		return userRepo.findAll();
	}

	public Object createUser(User user) {

		LocalDateTime currentDate = LocalDateTime.now();
		User userDB = userRepo.findByEmail(user.getEmail());

		if (userDB == null) {
			user.setUserId(UUID.randomUUID().toString());
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			user.setAccountExpiredDate(currentDate.plus(45, ChronoUnit.DAYS));
			user.setPasswordExpiredDate(currentDate.plus(45, ChronoUnit.DAYS));
			user.setAccountNonLocked(true);
			user.setEnabled(true);
			user.setFailedLoginAttempts(0);
			return userRepo.save(user);
		} else {
			return this.getHttpStatusResponse(null,1,null, 17, "User already existed");
		}
	}

	public UserResponse findUser() {
		User user = userRepo.findByEmail(this.getUserName());
		UserResponse userRes = new UserResponse();
		userRes.setUserName(user.getUserName());
		userRes.setEmail(user.getEmail());
		return userRes;
	}

	public HttpStatusResponse userLogin(String email, String password)
			throws javax.security.sasl.AuthenticationException {

		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		User user = userRepo.findByEmail(email);
		LocalDateTime currentDate = LocalDateTime.now();

		if (!user.isAccountNonLocked()) {
			return this.getHttpStatusResponse(null,1,null, 13, "Account is Locked");
		}
		if (!user.isEnabled()) {

			return this.getHttpStatusResponse(null,1,null, 12, "Account is Disabled");
		}

		try {

			this.doAuthenticate(userDetails, email, password);

		} catch (AccountExpiredException e) {

			return this.getHttpStatusResponse(null,1,null, 11, e.getMessage());

		} catch (CredentialsExpiredException e) {

			return this.getHttpStatusResponse(null,1,null, 10, e.getMessage());

		} catch (BadCredentialsException e) {

			user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
			userRepo.save(user);

			return this.getHttpStatusResponse(null,1,null, 9, e.getMessage());

		} catch (AuthenticationException e) {

			return this.getHttpStatusResponse(null,1,null, 8, e.getMessage());
		}

		user.setFailedLoginAttempts(0);
		user.setAccountExpiredDate(currentDate.plus(45, ChronoUnit.DAYS));
		userRepo.save(user);

		String token = this.helper.generateToken(userDetails);

		return this.getHttpStatusResponse(token,0,new HashMap<>(), 0, "Login Successfull");
		 
	}

	private void doAuthenticate(UserDetails userDetails, String email, String password)
			throws javax.security.sasl.AuthenticationException, LockedException {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email,
				password);
		manager.authenticate(authentication);
	}

	public HttpStatusResponse getHttpStatusResponse(String token, int statusCode,Map<String, Object> res,
			int errorCode, String message) {
		HttpStatusResponse response = new HttpStatusResponse();
		Map<String, Object> tokenData=res;
		if(tokenData!=null) {
			res.put("token", token);
		}
		response.setData(tokenData);
		response.setStatusCode(statusCode);
		response.setErrorCode(errorCode);
		response.setMessage(message);
		return response;
	}

	public HttpStatusResponse changePassword(GenerateNewPassword generatePassword) {

		String oldPass = generatePassword.getOldPassword();
		String newPass = generatePassword.getNewPassword();

		User user = userRepo.findByEmail(this.getUserName());

		if (!passwordEncoder.matches(newPass, user.getPassword())) {
			if (passwordEncoder.matches(oldPass, user.getPassword())) {

				user.setPassword(passwordEncoder.encode(newPass));
				userRepo.save(user);
				return this.getHttpStatusResponse(null,0,null, 18, "Password changed successfully...");
			} else {
				return this.getHttpStatusResponse(null,1,null, 19, "Please enter currect password");
			}
		} else {
			return this.getHttpStatusResponse(null,1,null, 20, "New password should different from old password");
		}
	}

	@Scheduled(fixedRate = 60000)
	public void unlockAccounts() {
		LocalDateTime now = LocalDateTime.now();
		List<User> users = userRepo.findByAccountNonLockedFalseAndLockedUntilBefore(now);
		for (User user : users) {
			user.setAccountNonLocked(true);
			user.setLockedUntil(null);
			user.setFailedLoginAttempts(0);
			userRepo.save(user);
		}
	}
	
	
	
	public HttpStatusResponse logout(HttpServletRequest request,HttpServletResponse response) {
		
		Authentication auth=SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			return this.getHttpStatusResponse(null,0,null, 21, "Logout successfull");
		}
		return this.getHttpStatusResponse(null,1,null, 22, "Failed");
	}
}
