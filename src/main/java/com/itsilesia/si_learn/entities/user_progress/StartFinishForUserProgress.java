package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.dtos.response.TwoIntsProgress;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.enums.ProgressStatusHelper;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class StartFinishForUserProgress {

	@CreationTimestamp
	@Column(name = "start_date", nullable = false, columnDefinition = "Date")
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = true, columnDefinition = "Date")
	private LocalDateTime endDate;

	public ProgressStatus progressStatus() {
		return ProgressStatusHelper.getStatus(startDate, endDate);
	}

	public abstract TwoIntsProgress progress();
}
