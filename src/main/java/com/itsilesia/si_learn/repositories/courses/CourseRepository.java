package com.itsilesia.si_learn.repositories.courses;

import com.itsilesia.si_learn.entities.courses.Course;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

import java.util.UUID;

public interface CourseRepository extends SpecificationJpaRepository<Course, UUID> {
}
