package com.itsilesia.si_learn.controllers;
import com.itsilesia.si_learn.dtos.response.ApiVersionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "API information", description = "General information about the API")
public class ApiInfoController {
	private final BuildProperties buildProperties;

	@GetMapping("/version")
	@ResponseStatus(HttpStatus.OK)
	@Operation(summary = "Get API version and build time")
	public ApiVersionResponse getVersion() {
		return new ApiVersionResponse(
			buildProperties.getVersion(),
			buildProperties.getTime()
		);
	}
}
