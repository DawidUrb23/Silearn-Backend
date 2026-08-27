package com.itsilesia.si_learn.dtos.request.compiler;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is a request that we are sending to the online compiler API.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OnlineCodeExecutionOutboundRequest {
	@NotBlank
	private String compiler;

	@NotBlank
	private String code;

	private String input;

	@JsonProperty("extra_params")
	private OnlineCodeExtraParams extraParams;
}