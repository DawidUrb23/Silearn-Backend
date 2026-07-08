package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateTaskRequest {
	private Float passingScore;
	@Size(max = 4 << 10)
	private String startingCode;
}
