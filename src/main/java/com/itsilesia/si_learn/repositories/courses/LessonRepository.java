package com.itsilesia.si_learn.repositories.courses;

import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.repositories.SpecificationJpaRepository;

import java.util.UUID;

public interface LessonRepository extends SpecificationJpaRepository<Lesson, UUID> {
}
