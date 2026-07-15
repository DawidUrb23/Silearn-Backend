package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.LessonResponse;
import com.itsilesia.si_learn.entities.courses.Lesson;
import com.itsilesia.si_learn.entities.user_progress.UserLesson;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;

@Getter
@Setter
public class MyLessonDto {
	private LessonResponse lesson;
	private UserProgressDto userProgress;

	public MyLessonDto(@NotNull Lesson lesson, UserLesson userLesson) {
		this.lesson = LessonResponse.fromEntity(lesson);
		if (userLesson != null)
			this.userProgress = UserProgressDto.fromEntity(userLesson);
	}

	static public MyLessonDto fromEntity(@NonNull Lesson lesson, UserLesson userLesson) {
		return new MyLessonDto(lesson, userLesson);
	}

	static public MyLessonDto fromEntity(@NonNull UserLesson userLesson) {
		return new MyLessonDto(userLesson.getLesson(), userLesson);
	}

	static public MyLessonDto fromEntity(Tuple<@NonNull Lesson, UserLesson> userAndProgress) {
		return new MyLessonDto(userAndProgress._1(), userAndProgress._2());
	}
}
