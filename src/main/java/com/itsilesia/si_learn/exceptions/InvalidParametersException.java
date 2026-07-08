package com.itsilesia.si_learn.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

///Bad Request (400)
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParametersException extends RuntimeException {
	public InvalidParametersException(String msg) {
		super(msg);
	}
}
