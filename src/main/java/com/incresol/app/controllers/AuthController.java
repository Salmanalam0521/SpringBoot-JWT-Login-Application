package com.incresol.app.controllers;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incresol.app.entities.User;
import com.incresol.app.models.GenerateNewPassword;
import com.incresol.app.models.HttpStatusResponse;
import com.incresol.app.models.JwtRequest;
import com.incresol.app.models.UserResponse;
import com.incresol.app.security.JwtHelper;
import com.incresol.app.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtHelper helper;

	// private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<HttpStatusResponse> login(@RequestBody JwtRequest request) throws AuthenticationException {

		HttpStatusResponse response = userService.userLogin(request.getEmail(), request.getPassword());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<Object> save(@RequestBody User user) {
		Object userFound = userService.createUser(user);

		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(userFound);
	}
	//@CrossOrigin(origins="http://localhost:4200")
	@GetMapping("/get-user")
	
	public UserResponse getUser() {
		UserResponse userResponse = userService.findUser();
		return userResponse;
	}

	@PutMapping("/new-password")
	public ResponseEntity<HttpStatusResponse> newPassword(@RequestBody GenerateNewPassword newPasswrod) {
		HttpStatusResponse message = userService.changePassword(newPasswrod);
		return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(message);
	}
	
	@DeleteMapping("/delete-user")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String delete() {
		return null;
	}
	@GetMapping("/logout")
	public HttpStatusResponse logout(HttpServletRequest request,HttpServletResponse response) {
		return userService.logout(request, response);
	}
}
