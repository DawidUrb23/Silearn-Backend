package com.itsilesia.si_learn.dtos.request.dictionary;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTranslationsInDictionaryRequest {
	@NotEmpty
	private List<UpdateTranslationDictionaryRequest> translations;
}
