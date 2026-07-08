package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.entities.enums.ContentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TaskContentRequest {
	@NotNull
	private ContentType type;
	@NotBlank
	private String content;
}
