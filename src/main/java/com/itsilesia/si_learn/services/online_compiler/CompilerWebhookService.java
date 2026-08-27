package com.itsilesia.si_learn.services.online_compiler;

import com.itsilesia.si_learn.dtos.request.compiler.CompilerWebhookCallback;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilerWebhookService {

	private final OnlineCompilerService onlineCompilerService;

	@Transactional
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void processCompilerResult(CompilerWebhookCallback payload) {
		try {
			log.info("processCompilerResult received: {}", new ObjectMapper().writeValueAsString(payload));
		} catch (Exception e) {
			System.console().printf(e.getMessage());
		}
		onlineCompilerService.saveExecutionResult(payload);
	}
}
