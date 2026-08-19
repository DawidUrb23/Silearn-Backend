package com.itsilesia.si_learn.dtos.request.dictionary;

import com.itsilesia.si_learn.validators.locale.ValidLocaleCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddRemoveLocaleRequest {
	@ValidLocaleCode
	private Locale languageCode;
}
