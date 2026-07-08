package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.dtos.request.dictionary.UpdateTranslationDictionaryRequest;
import com.itsilesia.si_learn.dtos.response.helper.DictionaryHelper;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.TaskContentRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskContentService {
	private final TaskContentRepository taskContentRepository;
	private final TranslationService translationService;
	private final CourseHelperService courseHelperService;


	public TaskContent updateContent(UUID taskContentId, TaskContentRequest taskContent, UUID userId) {
		TaskContent taskContentToUpdate = taskContentRepository.findById(taskContentId)
			.orElseThrow(() -> new ItemNotFoundException(TaskContent.class, taskContentId));
		courseHelperService.isAuthorOrThrow(taskContentToUpdate, userId);
		var originalLanguage = courseHelperService.getOriginalLanguage(taskContentToUpdate);

		var contentStr = taskContent.getContent();
		var originalContent = DictionaryHelper.getOriginalTranslation(taskContentToUpdate.getContent()).getValue();
		if (StringUtils.isNotBlank(contentStr) && !originalContent.equals(contentStr)) {
			translationService.updateTranslationDictionary(
				taskContentToUpdate.getContent().getId(),
				new UpdateTranslationDictionaryRequest(originalLanguage.getCode(), contentStr)
			);
		}
		if (taskContent.getType() != null)
			taskContentToUpdate.setType(taskContent.getType());
		return taskContentRepository.save(taskContentToUpdate);
	}

	public void delete(UUID taskContentId, UUID userId) {
		TaskContent taskContent = taskContentRepository.findById(taskContentId)
			.orElseThrow(() -> new ItemNotFoundException(TaskContent.class, taskContentId));
		courseHelperService.isAuthorOrThrow(taskContent, userId);
		taskContentRepository.delete(taskContent);
	}
}
