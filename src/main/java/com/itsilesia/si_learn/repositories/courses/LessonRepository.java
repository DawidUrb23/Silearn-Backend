package com.itsilesia.si_learn.repositories.courses;
import com.itsilesia.si_learn.entities.courses.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {
}
