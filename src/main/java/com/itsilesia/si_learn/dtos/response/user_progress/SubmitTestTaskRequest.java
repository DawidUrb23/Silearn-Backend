package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubmitTestTaskRequest {
	@NotBlank
	@Size(max = ValidationConstants.codeLenMax)
	private String code;
}
