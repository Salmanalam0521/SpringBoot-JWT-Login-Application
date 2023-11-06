package com.jwt.app.exception;

import javax.security.sasl.AuthenticationException;

public class AccountExpiredException extends AuthenticationException {
	public AccountExpiredException(String message) {
		super(message);
	}
}
