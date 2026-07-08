package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.dtos.response.courses.ModuleResponse;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MyModuleDetailsDto extends ModuleResponse {
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private TwoIntsProgress progress;

	public MyModuleDetailsDto(@NonNull UserModule userModule) {
		super(userModule.getModule());

		this.startDate = userModule.getStartDate();
		this.endDate = userModule.getEndDate();
		this.progressStatus = userModule.progressStatus();
		this.progress = userModule.progress();
	}

	static public MyModuleDetailsDto fromEntity(UserModule userModule) {
		return new MyModuleDetailsDto(userModule);
	}
}
