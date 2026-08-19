package com.itsilesia.si_learn.services.courses;

import com.itsilesia.si_learn.dtos.request.courses.TaskContentRequest;
import com.itsilesia.si_learn.entities.courses.TaskContent;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.courses.TaskContentRepository;
import com.itsilesia.si_learn.services.TranslationService;
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

		if (taskContent.getContent() != null)
			translationService.updateOriginalTranslation(taskContentToUpdate.getContent(), taskContent.getContent());

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
