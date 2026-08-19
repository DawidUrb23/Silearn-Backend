package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestScenarioRequest {
	@Size(max = ValidationConstants.codeInputLenMax)
	private String input;

	@Range(min = ValidationConstants.timeoutSecMin, max = ValidationConstants.timeoutSecMax)
	private Float timeoutSec;

	@Size(max = ValidationConstants.expectedOutputLenMax)
	private String expectedOutput;
}
