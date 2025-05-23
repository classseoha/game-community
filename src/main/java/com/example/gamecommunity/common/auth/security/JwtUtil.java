package com.example.gamecommunity.common.auth.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {

	/**
	 * JWT 토큰 앞에 붙는 "Bearer" 문자열 지정
	 * 토큰 인증 방식에서 사용되는 접두사로 그런의미로 prefix라는 이름을 붙인 것
	 * 의미는 HTTP 인증 타입을 명시하기 위한 것
	 */
	private static final String prefix = "Bearer ";
	/**
	 *  Java에서 정수 리터럴은 기본적으로 int 취급
	 *  L을 붙이면 long 타입으로 변경
	 *  1000L 는 1초를 나타내고 60을 곱해 1분, 다시 60을 곱해 1시간
	 */
	private static final long tokenLife = 60 * 60 * 1000L;

	/**
	 * @Value는 환경 변수나 설정 값들을 클래스의 필드에 주입할 때 사용 - 1
	 * secretKey는 .properties에 정의된 비밀키이고
	 * key는 JWT 서명에 사용할 Ket 객체
	 */
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	/**
	 * @PostConstruct(Bean 초기화 후 호출되는 메서드 지정) 어노테이션이 붙은 메서드는
	 * 객체의 의존성 주입이 완료된 후 자동으로 호출되도록 만듬 - 2
	 * Decoders.BASE64.decode로 디코딩하여 바이트 배열로 변환 - 3
	 * 바이트 배열을 사용하여 Ket 객체 생성후 할당 - 4
	 * 숫자는 동작 과정
	 */
	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * 토큰을 생성하는 메서드
	 *
	 * Date date = new Date();
	 * 여기서 new Date()는 현재 시간 기준으로 객체 생성
	 * 현재 시간 기준으로 JWT 발급 시점과 만료 시점을 정하기 위한 기준
	 *
	 * 앞서 만든 접두사 prefix와 토큰 생성과 파실하는 도구인 Jwts로 토큰 빌더 객체 생성해서 반환
	 * 순서대로
	 * Jwt의 subject 필드 설정이고 userId를 문자열로 변화해서 넣어둠
	 * 여기서 subject는 누구에게 발급 된것인가를 보통 의미한다
	 *
	 * 각각 email과 nickname을 클레임으로 설정한것
	 *
	 * 토큰 만료시간 설정, 현재 시간에 위에서 설정한 수명을 더해서 계산
	 *
	 * 토큰 발급 시간 설정
	 *
	 * 토큰을 서명, key는 Key 객체이고 HS256알고리즘 사용을 의미
	 *
	 * 설정 정보 기반으로 최종 JWT 토큰 문자열 생성
	 */
	public String createToken(Long userId, String email, String nickname) {
		Date date = new Date();

		return prefix + Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("email", email)
			.claim("nickname", nickname)
			.setExpiration(new Date(date.getTime() + tokenLife))
			.setIssuedAt(date)
			.signWith(key, SignatureAlgorithm.HS256)
			.compact();
	}

	/**
	 * Bearer 접두사를 제거하고 실제 JWT만 추출해주는 메서드
	 *
	 * 유효한 문자열이 아니거나 Bearer로 시작하면 예외를 발생 시킴
	 *
	 * 토큰을 반환하는데 7자리를 자르고 반환 - Bearer에 공백하나까지 포함해서 7자리
	 */
	public String subStringToken(String token) {
		if (!StringUtils.hasText(token) || !token.startsWith(prefix)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return token.substring(7);
	}

	/**
	 * JWT 토큰에서 payload 부분을 추출해 Claims 객체로 변환하는 메서드
	 *
	 * Jwt 파서를 생성하는 빌더 - 토큰 해석 준비
	 *
	 * 토큰 서명을 검증하기 위해 사용할 비밀 키 설정 - 안에 key값을 넣음
	 *
	 * 파서 객체 완성
	 *
	 * 전달받은 JWT 문자열(token)을 파싱하고 서명 검증
	 *
	 * 파싱 완료된 JWT 본문(Payload)을 Claims 객체로 가져옴
	 */
	public Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public long getTokenLife() {
		return tokenLife;  // tokenLife를 반환
	}

}
