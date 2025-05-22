package com.example.gamecommunity.common.auth.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration // Bean 등록
@EnableWebSecurity // Spring Security의 웹 보안 지원 활성화
// @PreAuthorize, @PostAuthorize, @Secured 등의 애너테이션 기반 권한 처리를 활성화
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;

	/**
	 * CSRF 보호 기능의 활성화 여부 - http.csrf
	 * 세션을 사용하지 않고 요청마다 인증 처리하도록 설정(STATELESS는 JWT 기반 인증에 필수로 사용) - .sessionManagement
	 * 특정 URL 요청에 대해 접근 권한 설정 - .authorizeHttpRequests
	 * 허용 URL 넣고 마지막에 .authenticated()로 그외 요청 인증 필요하도록 만듬 - .requestMatchers
	 * Security단에서 인증되지 않은 사용자 접근시 예최 처리 방식 정의 - .exceptionHandling
	 * JWT 토큰을 파싱하고 인증을 처리 - .addFilterBefore
	 * 설정을 마친 SecurityFilterChain을 Spring Security에 등록 - http.build();
	 */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/**", "/users/signup", "/error").permitAll()
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex
				.authenticationEntryPoint((request, response, authException) -> {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write("{\"status\":401,\"message\":\"인증이 필요합니다.\"}");
				})
			)
			.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// 패스워드 엔코더 메서드
	@Bean
	public PasswordEncoder passwordEncoder() {

		return new BCryptPasswordEncoder();
	}

	// JWT 인증 필터를 등록
	@Bean
	public JwtFilter jwtFilter() {
		return new JwtFilter(jwtUtil);

	}
}
