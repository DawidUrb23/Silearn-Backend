package com.itsilesia.si_learn.dtos.request.compiler;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * This is what we receive from the online compiler API.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompilerWebhookCallback {
	/// "success" or "error"
	private String status;
	/// Standard output (max 999 chars)
	private String output;
	/// Standard error / compilation errors (max 999 chars)
	@JsonAlias({"error", "errors"})
	private String error;
	/// null
	/// 0 = success
	/// 1 = Runtime error (e.g. uncaught exception, assertion failure)
	/// 2 = Misuse of command / invalid arguments
	/// 124 = Timeout — execution exceeded the 30-second limit
	/// 137 = 9 (SIGKILL)	Killed — memory limit exceeded or timeout
	/// 139 = 11 (SIGSEGV)	Segmentation fault — invalid memory access
	@JsonProperty("exit_code")
	private Integer exitCode;
	/// Signal number if killed (e.g. 9 for SIGKILL), null otherwise
	private Integer signal;
	/// Execution time in seconds (excludes compilation)
	private Double time;
	/// Total time in seconds (includes compilation)
	private Double total;
	/// Memory usage in KB
	private Double memory;

	/// Your custom data, passed through unchanged
	@JsonProperty("extra_params")
	private OnlineCodeExtraParams extraParams;
}