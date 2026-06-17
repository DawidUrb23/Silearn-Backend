package com.itsilesia.si_learn.dtos.response;

import com.itsilesia.si_learn.entities.BaseEntity;

import java.util.UUID;

public record IdOnlyDto(UUID id) {
	static public IdOnlyDto fromEntity(BaseEntity baseEntity) {
		return new IdOnlyDto(baseEntity.getId());
	}
}
