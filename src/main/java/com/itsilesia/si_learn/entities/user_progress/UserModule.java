package com.itsilesia.si_learn.entities.user_progress;


import com.itsilesia.si_learn.entities.compositeIds.UserModuleId;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.enums.ProgressStatus;
import com.itsilesia.si_learn.entities.enums.ProgressStatusHelper;
import com.itsilesia.si_learn.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "user_modules")
@IdClass(UserModuleId.class)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserModule {

	@Column(name = "start_date", nullable = false, columnDefinition = "Date")
	private LocalDateTime startDate;

	@Column(name = "end_date", nullable = true, columnDefinition = "Date")
	private LocalDateTime endDate;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;


	public ProgressStatus progressStatus() {
		return ProgressStatusHelper.getStatus(startDate, endDate);
	}
}
