package com.itsilesia.si_learn.config;

import com.itsilesia.si_learn.services.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A filter that processes JWT tokens from the Authorization header.
 * It validates the token, extracts user details, and sets the authentication
 * in the SecurityContextHolder. This allows for the use of `@AuthenticationPrincipal`
 * in controllers to access the authenticated user's ID.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			final var token = authHeader.substring(7);
			if (jwtProvider.isTokenValid(token)) {
				final Claims claims = jwtProvider.getClaims(token);
				final var userId = UUID.fromString(claims.getSubject());
				final var roles = jwtProvider.getRoles(token);
				final var authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

				final var authentication = new UsernamePasswordAuthenticationToken(userId, null, authorities);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response);
	}
}
