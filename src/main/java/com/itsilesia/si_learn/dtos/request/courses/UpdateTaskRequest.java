package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UpdateTaskRequest {
	private Integer index;
	private Float passingScore;
	@Size(max = 4 << 10)
	private String startingCode;
	private List<TaskContentRequest> taskContents;
}