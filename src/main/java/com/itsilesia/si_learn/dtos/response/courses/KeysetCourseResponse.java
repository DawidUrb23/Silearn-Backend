package com.itsilesia.si_learn.dtos.response.courses;

import com.itsilesia.si_learn.dtos.response.user_progress.MyCourseOverviewDto;

import java.util.List;

public record KeysetCourseResponse(
	List<MyCourseOverviewDto> data,
	String nextCursor
) {}
