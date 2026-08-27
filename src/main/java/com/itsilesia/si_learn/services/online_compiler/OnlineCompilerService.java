package com.itsilesia.si_learn.services.online_compiler;

import com.itsilesia.si_learn.dtos.request.compiler.CompilerWebhookCallback;
import com.itsilesia.si_learn.dtos.request.compiler.OnlineCodeExecutionOutboundRequest;
import com.itsilesia.si_learn.dtos.request.compiler.OnlineCodeExtraParams;
import com.itsilesia.si_learn.dtos.response.CompilationResponse;
import com.itsilesia.si_learn.dtos.response.users.OnlineCodeResponse;
import com.itsilesia.si_learn.entities.courses.Task;
import com.itsilesia.si_learn.entities.courses.TestScenario;
import com.itsilesia.si_learn.entities.enums.CompilationProgress;
import com.itsilesia.si_learn.entities.enums.ProgramingLanguage;
import com.itsilesia.si_learn.entities.user_progress.UserCodeExecution;
import com.itsilesia.si_learn.entities.user_progress.UserTask;
import com.itsilesia.si_learn.exceptions.ItemNotFoundException;
import com.itsilesia.si_learn.repositories.user_progress.UserCodeExecutionRepository;
import com.itsilesia.si_learn.services.admin.ProgramingLanguageCompilerService;
import com.itsilesia.si_learn.services.user_progress.UserAutoTestCheckService;
import com.itsilesia.si_learn.specifications.UserCodeExecutionSpecification;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class OnlineCompilerService {
	private final RestClient restClient;
	private final ProgramingLanguageCompilerService programingLanguageCompilerService;
	private final UserCodeExecutionRepository userCodeExecutionRepository;
	private final UserAutoTestCheckService userAutoTestCheckService;
	private final ExecutionCallbackService executionCallbackService;

	public OnlineCompilerService(@Value("${api.onlinecompiler.url}") String url, @Value("${api.onlinecompiler.key}") String key, ProgramingLanguageCompilerService programingLanguageCompilerService, UserCodeExecutionRepository userCodeExecutionRepository, UserAutoTestCheckService userAutoTestCheckService, ExecutionCallbackService executionCallbackService) {
		this.programingLanguageCompilerService = programingLanguageCompilerService;
		this.userCodeExecutionRepository = userCodeExecutionRepository;
		this.userAutoTestCheckService = userAutoTestCheckService;
		this.executionCallbackService = executionCallbackService;
		this.restClient = RestClient.builder()
			.baseUrl(url)
			.defaultHeader("Authorization", key)
			.build();
	}

	private String submitCodeAsync(OnlineCodeExecutionOutboundRequest request) {
		var retrieved = restClient.post()
			.uri("/api/run-code")
			.contentType(MediaType.APPLICATION_JSON)
			.body(request)
			.retrieve();

		return retrieved.body(OnlineCodeResponse.class).getId();
	}

	/**
	 * Runs code in the online compiler asynchronously, resolves compiler from programing language.
	 *
	 * @param code               user code to run
	 * @param input              user/auto tests input (std in)
	 * @param programingLanguage to resolve compiler from
	 * @return compilationId
	 */
	@Transactional
	private UUID executeRunOnlineAsync(@NotBlank String code, @Nullable String input, @NotNull ProgramingLanguage programingLanguage) {
		var languageCompiler = programingLanguageCompilerService.getById(programingLanguage);

		UUID compilationId = UUID.randomUUID();
		var onlineCodeExecutionRequest = new OnlineCodeExecutionOutboundRequest();
		onlineCodeExecutionRequest.setCode(code);
		onlineCodeExecutionRequest.setInput(input);
		onlineCodeExecutionRequest.setCompiler(languageCompiler.getCompiler());
		onlineCodeExecutionRequest.setExtraParams(new OnlineCodeExtraParams(compilationId));
		this.submitCodeAsync(onlineCodeExecutionRequest);
		return compilationId;
	}

	@Transactional
	public UUID executeRunOnlineAndSaveAsync(@NotNull UserTask userTask, @NotBlank String code, @Nullable String input) {
		var programingLanguage = userTask.getTask().getCourse().getProgramingLanguage();
		var compilationId = this.executeRunOnlineAsync(code, input, programingLanguage);
		this.saveUserCodeExecution(compilationId, userTask, null);
		return compilationId;
	}

	@Transactional
	public List<UserCodeExecution> executeRunTestsOnlineAndSaveAsync(@NotNull UserTask userTask, @NotNull Task task) {
		var programingLanguage = task.getCourse().getProgramingLanguage();
		var userSolution = userTask.getUserSolution();

		List<UserCodeExecution> executions = new ArrayList<>();
		UUID groupId = UUID.randomUUID();

		for (var scenario : task.getTestScenarios()) {
			var compilationId = this.executeRunOnlineAsync(userSolution, scenario.getInput(), programingLanguage);
			executions.add(
				this.makeUserCodeExecutionEntity(compilationId, userTask, scenario, groupId));
		}
		return userCodeExecutionRepository.saveAll(executions);
	}

	/**
	 * Callback from onlineCompiler with our Execution Results
	 *
	 * @param payload Execution Results
	 * @return saved entity
	 */
	@Transactional
	public UserCodeExecution saveExecutionResult(@NotNull CompilerWebhookCallback payload) {
		UUID compilationId = payload.getExtraParams().getCompilationId();
		var specification = UserCodeExecutionSpecification.hasCompilationId(compilationId);
		var userCodeExecution = userCodeExecutionRepository.findOne(specification)
			.orElseThrow(() -> new ItemNotFoundException(UserCodeExecution.class, compilationId));
		var compilationProgress = (payload.getStatus().equalsIgnoreCase(CompilationProgress.SUCCESS.name()) && (StringUtils.isBlank(payload.getError()) || payload.getExitCode() == 0)) ?
			CompilationProgress.SUCCESS : CompilationProgress.FAILED;
		userCodeExecution.setCompilationProgress(compilationProgress);
		userCodeExecution.setExitCode(payload.getExitCode());
		userCodeExecution.setCompilerOutput(compilationProgress == CompilationProgress.SUCCESS ?
			Optional.ofNullable(payload.getOutput()).orElse("") :
			"Exit Code: " + Optional.ofNullable(payload.getExitCode()).orElse(0).toString()
			+ " Error:\n" + Optional.ofNullable(payload.getError()).orElse(""));
		var savedExecution = userCodeExecutionRepository.save(userCodeExecution);

		UUID groupId = userCodeExecution.getGroupId();
		if (groupId != null) {
			//this is automated test (do not inform the user)
			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						CompletableFuture.runAsync(() -> userAutoTestCheckService.gradeUserTaskByCompilation(compilationId));
					}
				});
			} else {
				CompletableFuture.runAsync(() -> userAutoTestCheckService.gradeUserTaskByCompilation(compilationId));
			}
		} else {
			//this is normal execution (make a callback)
			if (TransactionSynchronizationManager.isActualTransactionActive()) {
				TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
					@Override
					public void afterCommit() {
						executionCallbackService.sendResultToUserCallback(savedExecution, payload);
					}
				});
			} else {
				executionCallbackService.sendResultToUserCallback(savedExecution, payload);
			}
		}
		return savedExecution;
	}

	@Transactional
	private UserCodeExecution saveUserCodeExecution(@NotNull UUID compilationId, @NotNull UserTask userTask, @Nullable TestScenario testScenario) {
		return userCodeExecutionRepository.save(
			this.makeUserCodeExecutionEntity(compilationId, userTask, testScenario, null));
	}

	private UserCodeExecution makeUserCodeExecutionEntity(@NotNull UUID compilationId, @NotNull UserTask userTask, @Nullable TestScenario testScenario, @Nullable UUID groupId) {
		var userCodeExecution = new UserCodeExecution();
		userCodeExecution.setUser(userTask.getUser());
		userCodeExecution.setTask(userTask.getTask());
		userCodeExecution.setUserTask(userTask);
		userCodeExecution.setCompilationId(compilationId);
		userCodeExecution.setTestScenario(testScenario);
		userCodeExecution.setCompilationProgress(CompilationProgress.PENDING);
		userCodeExecution.setGroupId(groupId);
		return userCodeExecution;
	}

	public CompilationResponse getCompilationResponse(UUID userId, UUID compilationId) {
		var specification = UserCodeExecutionSpecification.hasCompilationId(compilationId);
		var userCodeExecution = userCodeExecutionRepository.findOne(specification)
			.orElseThrow(() -> new ItemNotFoundException(UserCodeExecution.class, compilationId));
		var dto = new CompilationResponse();
		dto.setStatus(userCodeExecution.getCompilationProgress() == null ? CompilationProgress.PENDING : userCodeExecution.getCompilationProgress());
		if (userCodeExecution.getCompilationProgress() != null && userCodeExecution.getCompilationProgress() == CompilationProgress.SUCCESS)
			dto.setOutput(userCodeExecution.getCompilerOutput());
		else
			dto.setError(userCodeExecution.getCompilerOutput());
		return dto;
	}
}
