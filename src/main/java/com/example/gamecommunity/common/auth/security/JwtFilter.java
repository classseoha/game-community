package com.example.gamecommunity.common.auth.security;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
// OncePerRequestFilter를 상속받아서 모든 HTTP 요청마다 한 번만 실행되는 필터
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		// request로 받아오는 URL을 요청하여 String으로 url에 담는다
		String url = request.getRequestURI();

		// 요청이 도달했을 때 특정 경로만 필터를 통과시키도록 설정
		if (url.equals("/auth/login") || url.equals("/users/signup")) {
			filterChain.doFilter(request, response);
			return;
		}

		// "Authorization"이라는 이름의 헤더 값을 가져옴
		String bearerJwt = request.getHeader("Authorization");

		if (bearerJwt == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"status\":401,\"message\":\"Authorization 헤더가 존재하지 않습니다.\"}");
			return;
		}

		// jwtUtil의 메서드를 사용하여 토큰만 추출 - 'Bearer '제거
		String jwt = jwtUtil.subStringToken(bearerJwt);

		String tokenKey = "BLACKLIST_" + jwt;
		Boolean isBlacklisted = redisTemplate.hasKey(tokenKey);

		if (Boolean.TRUE.equals(isBlacklisted)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"status\":401,\"message\":\"로그아웃되었습니다. 다시 로그인 해주세요.\"}");
			return;
		}

		/**
		 * claims의 정보를 빼내서 담는다.
		 * 그 후에 예외처리 하고
		 * JWT에서 꺼낸 subject를 Principal로 설정
		 * 권한 정보는 빈 리스트로 설정
		 * 그 다음 설정한것들을 Spring Security의 전역 보안 컨텍스트에 인증 정보 저장
		 * 모든 인증 절차 후 다음 필터 또는 컨트롤러로 요청 전달
		 */
		try {
			Claims claims = jwtUtil.getClaims(jwt);
			if (claims == null) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.setContentType("application/json;charset=UTF-8");
				response.getWriter().write("{\"status\":401,\"message\":\"로그인이 필요합니다.\"}");
				return;
			}

			Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				List.of());
			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"status\":401,\"message\":\"로그인이 필요합니다.\"}");
			return;
		}

	}

}
