package com.itsilesia.si_learn.dtos.response.courses;

import java.util.List;

public record KeysetCourseResponse(
	List<CourseResponse> data,
	String nextCursor
) {}
