package com.itsilesia.si_learn.dtos.response;

import com.itsilesia.si_learn.entities.enums.CompilationProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the compiler response after executing the code
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponse {
	private String error;
	private String output;
	private CompilationProgress status;
}
