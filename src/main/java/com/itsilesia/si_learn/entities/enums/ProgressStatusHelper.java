package com.itsilesia.si_learn.entities.enums;

import java.time.LocalDateTime;

public final class ProgressStatusHelper {

	private ProgressStatusHelper() {
	}

	/**
	 * Determines the ProgressStatus based on the start and end dates.
	 *
	 * @param startDate The date the activity started (can be null)
	 * @param endDate   The date the activity ended (can be null)
	 * @return ProgressStatus matching the current state
	 */
	public static ProgressStatus getStatus(LocalDateTime startDate, LocalDateTime endDate) {
		// 1. If it has an end date, it is FINISHED (regardless of start date)
		if (endDate != null) {
			return ProgressStatus.FINISHED;
		}

		// 2. If it has a start date (and no end date), it is IN_PROGRESS
		if (startDate != null) {
			return ProgressStatus.IN_PROGRESS;
		}

		// 3. Otherwise (both are null, or no start date), it is NOT_STARTED
		return ProgressStatus.NOT_STARTED;
	}
}
