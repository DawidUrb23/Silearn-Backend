package com.itsilesia.si_learn.controllers.admin;

import com.itsilesia.si_learn.dtos.request.admin.ProgramingLanguageCompilerUpsertRequest;
import com.itsilesia.si_learn.dtos.response.admin.ProgramingLanguageCompilerResponse;
import com.itsilesia.si_learn.dtos.response.exceptions.ErrorDetailsDto;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.services.admin.ProgramingLanguageCompilerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/programing-languages")
@RequiredArgsConstructor
@Validated
@Tag(name = "Programing Languages", description = "Admin endpoints for managing Programing Languages and their compilers (Admin)")
public class ProgramingLanguageController {
	private final ProgramingLanguageCompilerService programingLanguageCompilerService;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get all compilers for programing languages")
	public List<ProgramingLanguageCompilerResponse> programingLanguageCompilerGetAll() {
		return programingLanguageCompilerService.getAll()
			.stream().map(ProgramingLanguageCompilerResponse::FromEntity).toList();
	}

	@GetMapping("/{language}")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get compiler for programing language")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	@ApiResponse(responseCode = "404", description = "Compiler for programing language  not found",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ProgramingLanguageCompilerResponse programingLanguageCompilerGet(@PathVariable ProgramingLanguage language) {
		return ProgramingLanguageCompilerResponse.FromEntity(
			programingLanguageCompilerService.getById(language));
	}

	@PutMapping
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Add or Update compiler for programing language")
	@ApiResponse(responseCode = "400", description = "Invalid request body",
		content = @Content(schema = @Schema(implementation = ErrorDetailsDto.class)))
	public ProgramingLanguageCompilerResponse programingLanguageCompilerUpdate(@RequestBody @Valid ProgramingLanguageCompilerUpsertRequest request) {
		return ProgramingLanguageCompilerResponse.FromEntity(
			programingLanguageCompilerService.update(request));
	}
}
