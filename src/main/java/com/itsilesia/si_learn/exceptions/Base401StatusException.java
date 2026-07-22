package com.itsilesia.si_learn.exceptions;

import org.springframework.http.HttpStatus;

public abstract class Base401StatusException extends BaseStatusException {
	public Base401StatusException(String message) {

		super(HttpStatus.UNAUTHORIZED, message);
	}
}
