package com.itsilesia.si_learn.dtos.request.compiler;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubmitExecuteTaskRequest {
	@NotBlank
	@Size(max = ValidationConstants.codeLenMax)
	private String code;

	@Size(max = ValidationConstants.codeInputLenMax)
	private String input;
}
