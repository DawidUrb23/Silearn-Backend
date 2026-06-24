package com.itsilesia.si_learn.dtos.request.courses;

import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.entities.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UpdateTaskContentRequest {
	private int index;
	private ContentType type;
	private UpdateTranslationDictionaryRequest request;
}