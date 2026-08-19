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
public class UpdateTaskRequest {
	@Range(min = ValidationConstants.gradeMinScore, max = ValidationConstants.gradeMaxScore)
	private Float passingScore;
	@Size(max = ValidationConstants.codeLenMax)
	private String startingCode;
}