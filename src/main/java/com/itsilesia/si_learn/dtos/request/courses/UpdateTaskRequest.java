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
	@Size(max = 4 * 1024)
	private String startingCode;
	private Float passingScore;
	private List<UpdateTaskContentRequest> taskContent;
}