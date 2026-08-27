package com.itsilesia.si_learn.exceptions;

import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/// Not Found (404)
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class CompilerNotFoundException extends Base404StatusException {
	private final Object id;

	public CompilerNotFoundException(ProgramingLanguage id) {
		super(String.format("Compiler not found for programing language: %s", id));
		this.id = id;
	}
}
