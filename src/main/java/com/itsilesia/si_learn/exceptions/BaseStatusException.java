package com.itsilesia.si_learn.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public abstract class BaseStatusException extends RuntimeException {

	private final HttpStatus httpStatus;

	public BaseStatusException(HttpStatus httpStatus,String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

}
