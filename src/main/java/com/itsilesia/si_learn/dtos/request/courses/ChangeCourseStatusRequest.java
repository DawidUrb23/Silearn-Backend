package com.itsilesia.si_learn.dtos.request.courses;
import com.itsilesia.si_learn.entities.enums.VisibilityStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeCourseStatusRequest(@NotNull VisibilityStatus status) {
}
