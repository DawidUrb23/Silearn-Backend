package com.itsilesia.si_learn.dtos.request.dictionary;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class GetUntranslatedDictionariesFilters {
	@Nullable
	Boolean autoTranslatable;
	@Nullable
	LocalDate createdAfter;
	@Nullable
	LocalDate updatedAfter;
	@Nullable
	UUID courseId;
}
