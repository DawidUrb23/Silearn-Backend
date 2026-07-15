package com.itsilesia.si_learn.dtos.request.courses;

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
	@Size(max = 80, message = "Topic is too Long")
	private String topic;

	@Size(max = 2300, message = "Description is too long")
	private String description;
}
