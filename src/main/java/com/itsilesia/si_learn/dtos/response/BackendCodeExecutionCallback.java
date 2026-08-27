package com.itsilesia.si_learn.dtos.response;

import com.itsilesia.si_learn.dtos.request.compiler.CompilerWebhookCallback;
import com.itsilesia.si_learn.entities.enums.CompilationProgress;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BackendCodeExecutionCallback {
	private UUID compilationId;
	private UUID userId;
	private UUID taskId;
	private CompilationProgress status;
	private String output;
	private String error;
	private Integer exitCode;
	private Double time;

	public BackendCodeExecutionCallback(UserCodeExecution savedExecution, @NotNull CompilerWebhookCallback payload) {
		CompilationProgress progress = savedExecution.getCompilationProgress();
		this.compilationId = savedExecution.getCompilationId();
		this.userId = savedExecution.getUser().getId();
		this.taskId = savedExecution.getTask().getId();
		this.status = progress;
		this.output = payload.getOutput();
		this.error = payload.getError();
		this.exitCode = payload.getExitCode();
		this.time = payload.getTime();
	}
}
