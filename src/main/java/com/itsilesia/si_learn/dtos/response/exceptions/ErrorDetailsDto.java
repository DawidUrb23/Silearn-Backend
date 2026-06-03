package com.itsilesia.si_learn.dtos.response.exceptions;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NotNull
public class ErrorDetailsDto {
	private final OffsetDateTime timestamp = OffsetDateTime.now(); // UTC Format
	private String message;
}
