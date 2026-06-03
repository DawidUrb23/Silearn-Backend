package com.itsilesia.si_learn.repositories;

import com.itsilesia.si_learn.entities.courses.Course;

import java.util.UUID;

public interface CourseRepository extends SpecificationJpaRepository<Course, UUID> {
}
