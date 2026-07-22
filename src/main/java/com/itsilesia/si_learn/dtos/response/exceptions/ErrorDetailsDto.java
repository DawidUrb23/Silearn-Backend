package com.itsilesia.si_learn.dtos.response.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NotNull
public class ErrorDetailsDto {
	private final OffsetDateTime timestamp = OffsetDateTime.now(); // UTC Format
	private final String message;
	private final HttpStatus httpStatus;
}
