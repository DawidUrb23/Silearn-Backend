package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.entities.enums.ContentType;
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
	private Integer index;
	private ContentType contentType;
	@NotNull
	private UpdateTranslationDictionaryRequest translation;
}
