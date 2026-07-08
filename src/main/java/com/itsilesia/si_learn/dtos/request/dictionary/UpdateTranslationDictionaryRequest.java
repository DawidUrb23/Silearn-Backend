package com.itsilesia.si_learn.dtos.request.dictionary;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTranslationDictionaryRequest {
	private String languageCode;

	//fixme: this could be very restrictive for TaskContent
	@Size(max = 4 << 10)
	private String content;
}
