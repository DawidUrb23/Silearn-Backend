package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.config.ValidationConstants;
import com.itsilesia.si_learn.entities.enums.ContentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

	@NotNull
	@Size(max = ValidationConstants.translationDictItemContentMaxLen)
	private String content;
}
