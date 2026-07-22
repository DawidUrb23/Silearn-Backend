package com.itsilesia.si_learn.dtos.response;
import java.time.Instant;

public record ApiVersionResponse (
	String version,
	Instant buildTime
){
}
