package com.itsilesia.si_learn.dtos.request.compiler;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OnlineCodeExtraParams {
	@NotNull
	@JsonProperty("compilationId")
	private UUID compilationId;
}
