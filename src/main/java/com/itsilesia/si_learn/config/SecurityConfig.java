package com.itsilesia.si_learn.config;

import com.itsilesia.si_learn.entities.enums.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthFilter jwtAuthFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

				// `api/courses` can be accessed by anyone, but only Teacher can make changes to it
				.requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
				.requestMatchers("/api/courses/**").hasRole(Role.TEACHER.name())

				// `api/modules` can be accessed by anyone, but only Teacher can make changes to it
				.requestMatchers(HttpMethod.GET, "/api/modules/**").permitAll()
				.requestMatchers("/api/modules/**").hasRole(Role.TEACHER.name())

				// `api/translations` can be accessed by anyone, but only Translator/Administrator can make changes to it
				.requestMatchers(HttpMethod.GET, "/api/translations/**").permitAll()
				.requestMatchers("/api/translations/**").hasAnyRole(Role.TRANSLATOR.name(), Role.ADMINISTRATOR.name())

				// Admin has access to admin panel
				.requestMatchers("/api/admin/**").hasRole(Role.ADMINISTRATOR.name())

				// Admin has access to users panel
				.requestMatchers("/api/users/**").hasRole(Role.ADMINISTRATOR.name())

				// any user can get and change their account info
				.requestMatchers("/api/account/**").authenticated()

				.anyRequest().permitAll()   //defaults to anyone can access (might want to change that to `authenticated()`)
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((request, response, authException) ->
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())
				)
			)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
