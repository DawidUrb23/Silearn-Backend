package com.itsilesia.si_learn.services.online_compiler;

import com.itsilesia.si_learn.dtos.request.compiler.CompilerWebhookCallback;
import com.itsilesia.si_learn.dtos.response.BackendCodeExecutionCallback;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ExecutionCallbackService {

	private final String webhookUrl;
	private final RestClient restClient;

	public ExecutionCallbackService(
		@Value("${app.frontend.webhook-url:}") String webhookUrl
	) {
		this.webhookUrl = webhookUrl;
		this.restClient = RestClient.builder().build();
	}

	public void sendResultToUserCallback(UserCodeExecution savedExecution, @NotNull CompilerWebhookCallback payload) {
		if (StringUtils.isBlank(webhookUrl)) {
			log.debug("Frontend webhook URL is not configured, skipping callback.");
			return;
		}

		CompletableFuture.runAsync(() -> {
			try {
				BackendCodeExecutionCallback callbackPayload = new BackendCodeExecutionCallback(savedExecution, payload);
				restClient.post()
					.uri(webhookUrl)
					.contentType(MediaType.APPLICATION_JSON)
					.body(callbackPayload)
					.retrieve()
					.toBodilessEntity();
				log.info("Successfully sent execution callback to {} for compilationId: {}", webhookUrl, savedExecution.getCompilationId());
			} catch (Exception e) {
				log.error("Failed to send execution callback to {} for compilationId: {}. Error: {}", webhookUrl, savedExecution.getCompilationId(), e.getMessage(), e);
			}
		});
	}
}
