package com.example.gamecommunity.common.auth.security;

import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
// OncePerRequestFilter를 상속받아서 모든 HTTP 요청마다 한 번만 실행되는 필터
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // request로 받아오는 URL을 요청하여 String으로 url에 담는다
        String url = request.getRequestURI();

        // 요청이 도달했을 때 특정 경로만 필터를 통과시키도록 설정
        if (url.equals("/auth/login") || url.equals("/users/signup") || url.equals("/v1/posts/search") || url.equals("/v2/posts/search") || url.equals("/v3/posts/search") || url.equals("/v3/posts/search/rank")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Authorization"이라는 이름의 헤더 값을 가져옴
        String bearerJwt = request.getHeader("Authorization");

        if (bearerJwt == null) {
            throw new CustomException(ErrorCode.SC_BAD_REQUEST);
        }

        // jwtUtil의 메서드를 사용하여 토큰만 추출 - 'Bearer '제거
        String jwt = jwtUtil.subStringToken(bearerJwt);

        /**
         * claims의 정보를 빼내서 담는다.
         * 그 후에 예외처리 하고
         * JWT에서 꺼낸 subject를 Principal로 설정
         * 권한 정보는 빈 리스트로 설정
         * 그 다음 설정한것들을 Spring Security의 전역 보안 컨텍스트에 인증 정보 저장
         * 모든 인증 절차 후 다음 필터 또는 컨트롤러로 요청 전달
         */
        try {

            // String tokenKey = "BLACKLIST_" + jwt;
            // if (redisTemplate.hasKey(tokenKey)) {
            // 	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그아웃된 사용자입니다.");
            // 	return;
            // }

            Claims claims = jwtUtil.getClaims(jwt);
            if (claims == null) {
                throw new CustomException(ErrorCode.SC_BAD_REQUEST);
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, List.of()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.SC_BAD_REQUEST);
        }

    }

}
