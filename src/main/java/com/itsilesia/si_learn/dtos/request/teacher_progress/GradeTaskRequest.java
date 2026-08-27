package com.itsilesia.si_learn.dtos.request.teacher_progress;

import com.itsilesia.si_learn.config.ValidationConstants;
import org.hibernate.validator.constraints.Range;

public record GradeTaskRequest(
	@Range(min = ValidationConstants.gradeMinScore, max = ValidationConstants.gradeMaxScore)
	Float grade
) {
}
