package com.itsilesia.si_learn.controllers;

import com.itsilesia.si_learn.dtos.request.compiler.CompilerWebhookCallback;
import com.itsilesia.si_learn.services.online_compiler.CompilerWebhookService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/compiler")
@RequiredArgsConstructor
@Hidden
public class WebhookController {

	private final CompilerWebhookService compilerWebhookService;

	@PostMapping("/webhook")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void receiveCompilerResult(@RequestBody CompilerWebhookCallback payload) {
		compilerWebhookService.processCompilerResult(payload);
	}

}
