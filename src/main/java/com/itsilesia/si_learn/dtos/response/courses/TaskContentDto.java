package com.itsilesia.si_learn.dtos.response.courses;


import com.itsilesia.si_learn.dtos.response.dictonary.TranslationDto;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.entities.enums.ContentType;
import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskContentDto {
	private UUID id;
	private ContentType type;
	private TranslationDto content;

	public static TaskContentDto fromEntity(TaskContent content) {
		return TaskContentDto.builder()
			.id(content.getId())
			.type(content.getType())
			.content(TranslationDto.fromEntity(
				DictionaryHelper.getTranslationWithCode(content.getContent())
			))
			.build();
	}
}