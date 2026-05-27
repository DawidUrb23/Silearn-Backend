package com.example.demo.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

///Bad Request (400)
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidParametersException extends RuntimeException {
	public InvalidParametersException(String msg) {
		super(msg);
	}
}
