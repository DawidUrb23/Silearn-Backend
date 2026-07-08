package com.itsilesia.si_learn.dtos.response.user_progress;

import com.itsilesia.si_learn.dtos.response.courses.ModuleResponse;
import com.itsilesia.si_learn.entities.courses.Module;
import com.itsilesia.si_learn.entities.user_progress.UserModule;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.yaml.snakeyaml.util.Tuple;

@Getter
@Setter
public class MyModuleDto {
	private ModuleResponse module;
	private UserProgressDto userProgress;

	public MyModuleDto(@NonNull Module module, UserModule userModule) {
		this.module = ModuleResponse.fromEntity(module);
		if (userModule != null)
			this.userProgress = UserProgressDto.fromEntity(userModule);
	}

	static public MyModuleDto fromEntity(@NonNull Module module, UserModule userModule) {
		return new MyModuleDto(module, userModule);
	}

	static public MyModuleDto fromEntity(Tuple<@NonNull Module, UserModule> userAndModule) {
		return new MyModuleDto(userAndModule._1(), userAndModule._2());
	}
}
