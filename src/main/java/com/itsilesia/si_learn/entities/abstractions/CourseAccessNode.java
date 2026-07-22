package com.itsilesia.si_learn.entities.abstractions;

import com.itsilesia.si_learn.entities.courses.Course;

import java.util.Collection;
import java.util.UUID;

public interface CourseAccessNode {
	int getIndex();

	UUID getId();

	Collection<? extends CourseAccessNode> getSiblings();

	CourseAccessNode getParentNode();

	Course getCourse();

}
