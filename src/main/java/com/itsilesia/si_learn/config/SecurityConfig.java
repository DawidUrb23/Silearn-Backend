package com.itsilesia.si_learn.config;

import com.itsilesia.si_learn.entities.enums.Role;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
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
			.authorizeHttpRequests(authorize -> {
				new ConfigureAuthority(authorize)
					.teacherApi() // api/teacher
					.coursesApi() // api/courses
					.modulesApi() // api/modules
					.lessonsApi() // api/lessons
					.tasksApi() // api/tasks
					.testScenariosApi() // api/test-scenarios
					.adminApi()   // api/admin
					.translationsApi()   // api/translations
					.myApis()   // api/my/*

					.otherApis();   // other
			})
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exception -> exception
				.authenticationEntryPoint((request, response, authException) ->
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage())
				)
			)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@RequiredArgsConstructor
	@Getter
	private static class ConfigureAuthority {
		private final AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize;

		/// (everyone/authenticated users) have access to those endpoints
		public ConfigureAuthority otherApis() {
			authorize
				.requestMatchers("/api/auth/refresh").authenticated()
				.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
				// any user can get and change their account info
				.requestMatchers("/api/account/**").authenticated()


				.anyRequest().permitAll();  //defaults to anyone can access (might want to change that to `authenticated()`)
			return this;
		}

		/// `api/courses` can be accessed by anyone, only Teacher can make changes to it, only singed in users can enroll
		public ConfigureAuthority coursesApi() {
			authorize
				.requestMatchers(HttpMethod.GET, "/api/courses").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/courses/*").permitAll()
				.requestMatchers("/api/courses/*/enroll").authenticated()
				.requestMatchers("/api/courses/**").hasAuthority(Role.TEACHER.name());
			return this;
		}

		//TODO: see if this is not duplicated after merge

		/// `api/teacher` can be accessed by Teacher
		public ConfigureAuthority teacherApi() {
			authorize
				.requestMatchers("/api/teacher/**").hasAuthority(Role.TEACHER.name());
			return this;
		}

		/// `api/modules` can be accessed by Owner Teacher
		public ConfigureAuthority modulesApi() {
			authorize
//				.requestMatchers(HttpMethod.GET, "/api/modules/**").permitAll()
				.requestMatchers("/api/modules/**").hasAuthority(Role.TEACHER.name());
			return this;
		}

		/// Admin has access to admin panel & users panel
		public ConfigureAuthority adminApi() {
			authorize
				// Admin has access to admin panel
				.requestMatchers("/api/admin/**").hasAuthority(Role.ADMINISTRATOR.name())
				// Admin has access to users panel
				.requestMatchers("/api/users/**").hasAuthority(Role.ADMINISTRATOR.name());
			return this;
		}

		/// `api/translations` can be accessed by anyone, but only Translator/Administrator can make changes to it
		public ConfigureAuthority translationsApi() {
			authorize
				.requestMatchers(HttpMethod.GET, "/api/translations").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/translations/*").permitAll()
				.requestMatchers("/api/translations/languages").hasAnyAuthority(Role.ADMINISTRATOR.name())
				.requestMatchers("/api/translations/**").hasAnyAuthority(Role.TRANSLATOR.name(), Role.ADMINISTRATOR.name());
			return this;
		}

		/// `api/my/*` can be accessed by logged-in users
		public ConfigureAuthority myApis() {
			authorize
				// `api/my/courses` can be accessed by logged-in users
				.requestMatchers("/api/my/courses/**").authenticated()
				.requestMatchers("/api/my/modules/**").authenticated()
				.requestMatchers("/api/my/lessons/**").authenticated()
				.requestMatchers("/api/my/tasks/**").authenticated();
			return this;
		}


		/// `api/lessons` can be accessed by Owner Teacher
		public ConfigureAuthority lessonsApi() {
			authorize
//				.requestMatchers(HttpMethod.GET, "/api/lessons/**").permitAll()
				.requestMatchers("/api/lessons/**").hasAuthority(Role.TEACHER.name());
			return this;
		}

		/// `api/tasks` can be accessed by Owner Teacher
		public ConfigureAuthority tasksApi() {
			authorize
//				.requestMatchers(HttpMethod.GET, "/api/tasks/**").permitAll()
				.requestMatchers("/api/tasks/**").hasAuthority(Role.TEACHER.name())
				//allow only the teacher(owner) to grade students
				.requestMatchers("/api/teacher-check/**").hasAuthority(Role.TEACHER.name());
			return this;
		}

		/// `api/test-scenarios` can be accessed by Owner Teacher
		public ConfigureAuthority testScenariosApi() {
			authorize
//				.requestMatchers(HttpMethod.GET, "/api/test-scenarios/**").permitAll()
				.requestMatchers("/api/test-scenarios/**").hasAuthority(Role.TEACHER.name());
			return this;
		}
	}

}
