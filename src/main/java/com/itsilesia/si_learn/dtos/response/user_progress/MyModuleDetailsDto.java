package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.dtos.response.courses.ModuleResponse;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyModuleDetailsDto {
	private ModuleResponse module;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private TwoIntsProgress progress;

	static public MyModuleDetailsDto fromEntity(UserModule userModule) {
		return new MyModuleDetailsDto(
			ModuleResponse.fromEntity(userModule.getModule()),
			userModule.getStartDate(),
			userModule.getEndDate(),
			userModule.progressStatus(),
			userModule.progress()
		);
	}
}
