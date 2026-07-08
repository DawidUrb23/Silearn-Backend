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

	public TaskContentDto(TaskContent content) {
		this.id = content.getId();
		this.type = content.getType();
		this.content = TranslationDto.fromEntity(
			DictionaryHelper.getTranslationWithCode(content.getContent())
		);
	}

	public static TaskContentDto fromEntity(TaskContent content) {
		return content == null ? null : new TaskContentDto(content);
	}
}