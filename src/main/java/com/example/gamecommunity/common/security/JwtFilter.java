package com.example.gamecommunity.common.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException { 
		String url = request.getRequestURI();

		if (url.equals("/auth/login") || url.equals("/users/signup")) {
			filterChain.doFilter(request, response);
			return;
		}

		String bearerJwt = request.getHeader("Authorization");

		if (bearerJwt == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
			return;
		}

		String jwt = jwtUtil.subStringToken(bearerJwt);

		try {
			Claims claims = jwtUtil.getClaims(jwt);
			if (claims == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
				return;
			}

			// request.setAttribute("userId", Long.parseLong(claims.getSubject()));
			// request.setAttribute("email", claims.get("email"));
			// request.setAttribute("nickname", claims.get("nickname"));

			Authentication authentication = new UsernamePasswordAuthenticationToken(
				claims.getSubject(), null, List.of()
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			filterChain.doFilter(request, response);
		} catch (Exception e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효하지 않은 JWT 토큰입니다.");
		}

	}

}
