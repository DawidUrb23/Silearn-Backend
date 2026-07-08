package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.user_progress.StartFinishForUserProgress;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserProgressDto {
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ProgressStatus progressStatus;
	private TwoIntsProgress progress;

	public UserProgressDto(@NotNull StartFinishForUserProgress userProgress) {
		this.startDate = userProgress.getStartDate();
		this.endDate = userProgress.getEndDate();
		this.progressStatus = userProgress.progressStatus();
		this.progress = userProgress.progress();
	}

	static public UserProgressDto fromEntity(StartFinishForUserProgress userProgress) {
		if (userProgress == null) return null;
		return new UserProgressDto(userProgress);
	}
}
