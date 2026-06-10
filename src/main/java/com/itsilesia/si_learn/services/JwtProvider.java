package com.itsilesia.si_learn.services;

import com.itsilesia.si_learn.entities.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtProvider {

	@Value("${jwt.secret}")
	private String secret;

	private JwtParser jwtParser;

	@PostConstruct
	protected void init() {
		jwtParser = Jwts.parser().verifyWith(getSigningKey()).build();
	}

	public String generateToken(User user) {
		Instant now = Instant.now();
		Instant expiration = now.plus(1, ChronoUnit.DAYS);

		return Jwts.builder()
			.subject(user.getId().toString())
			.claim("roles", user.getRoles().stream().map(role -> role.getRole().name()).collect(Collectors.toList()))
			.issuedAt(Date.from(now))
			.expiration(Date.from(expiration))
			.signWith(getSigningKey())
			.compact();
	}

	public boolean isTokenValid(String token) {
		try {
			jwtParser.parseSignedClaims(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isTokenExpired(String token) {
		return getClaims(token).getExpiration().before(new Date());
	}

	public String getSubject(String token) {
		return getClaims(token).getSubject();
	}

	public List<String> getRoles(String token) {
		return getClaims(token).get("roles", List.class);
	}

	public Claims getClaims(String token) {
		return jwtParser.parseSignedClaims(token).getPayload();
	}

	private SecretKeySpec getSigningKey() {
		return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
	}

}
