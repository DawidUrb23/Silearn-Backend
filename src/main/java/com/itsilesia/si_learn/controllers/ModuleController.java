package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.courses.CreateModuleRequest;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.services.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {
	private final ModuleService moduleService;

	@PostMapping("/courses/{courseId}")
	public Module createModule(
		@PathVariable UUID courseId,
		@Valid @RequestBody CreateModuleRequest request
	) {
		return moduleService.createModule(courseId, request);
	}
}
