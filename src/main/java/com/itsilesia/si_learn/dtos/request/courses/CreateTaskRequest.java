package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateTaskRequest {
	private Integer index;
	private Float passingScore;
	@Size(max = 4 * 1024)
	private String startingCode;
	//fixme: pass it from url path
	private UUID lessonId;
}
