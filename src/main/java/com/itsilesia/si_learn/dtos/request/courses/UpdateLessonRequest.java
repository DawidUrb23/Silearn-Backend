package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLessonRequest {
	@Size(min = ValidationConstants.titleLenMin, max = ValidationConstants.titleLenMax)
	private String topic;

	@Size(min = ValidationConstants.descriptionLenMin, max = ValidationConstants.descriptionLenMax)
	private String description;
}
