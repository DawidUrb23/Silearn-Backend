package com.itsilesia.si_learn.dtos.request.dictionary;

import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddLocaleRequest {
	@NotBlank
	@ValidLocaleCode
	private String languageCode;
}
