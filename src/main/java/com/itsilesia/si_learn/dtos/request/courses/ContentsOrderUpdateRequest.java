package com.itsilesia.si_learn.dtos.request.courses;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ContentsOrderUpdateRequest {
	@NotNull
	@NotEmpty
	private List<UUID> contents;
}
