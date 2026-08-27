package com.itsilesia.si_learn.scheduler;

import com.itsilesia.si_learn.services.online_compiler.UserCodeExecutionCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class CompilationCleanupScheduler {
	private final UserCodeExecutionCleanupService userCodeExecutionCleanupService;

	@Scheduled(
		fixedDelayString = "${api.onlinecompiler.cleanup.interval-minutes:20}",
		timeUnit = TimeUnit.MINUTES
	)
	public void scheduleTaskCleanup() {
		log.info("UserCodeExecution: running scheduled cleanup of outdated user compilations...");
		var amount = userCodeExecutionCleanupService.cleanupOutdated();
		log.info("UserCodeExecution: Removed {} outdated item/s.", amount);
	}
}
