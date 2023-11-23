package com.incresol.app.exception;

import javax.security.sasl.AuthenticationException;

public class AccountExpiredException extends AuthenticationException {
	public AccountExpiredException(String message) {
		super(message);
	}
}
