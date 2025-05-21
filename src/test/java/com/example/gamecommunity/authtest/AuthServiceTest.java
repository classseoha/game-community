package com.example.gamecommunity.authtest;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.auth.service.AuthService;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.common.security.JwtUtil;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

public class AuthServiceTest {

	// 의존성들 모킹
	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserRepository userRepository;

	@Mock
	private JwtUtil jwtUtil;

	// 위에서 만든 mock들을 AuthService에 자동 주입하여 테스트 가능하게 설정
	@InjectMocks
	private AuthService authService;

	// 테스트에 사용할 사용자 객체를 위한 필드
	private User user;

	@BeforeEach // 각 테스트 메소드 실행 전에 실행되는 메소드 지정
	void setUp() throws Exception {
		// 위에 설정한 어노테이션(@Mock, @InjectMocks)이 실제 동작하도록 초기화
		MockitoAnnotations.openMocks(this);
		// 실제 테스트용 User 객체 생성
		user = new User("test@example.com", "encoded-password", "test-nickname");

		// User 클래스의 private Long id 필드를 가져옴
		Field idField = User.class.getDeclaredField("id");
		idField.setAccessible(true); // private 필드를 외부에서 접근 가능하게 만듬
		idField.set(user, 1L); // id 강제 설정
	}

	@Test
	// 성공 테스트
	void testLogin_success() {
		// 테스트용 로그인 요청 Dto 생성
		SigninRequestDto signinRequestDto = new SigninRequestDto("test@example.com", "password");

		// 이메일로 유저를 찾는 로직 모킹, 여기서는 가짜 user 반환
		when(userRepository.findByEmailOrElseThrow(signinRequestDto.getEmail())).thenReturn(user);
		// 비밀번호 비교로 일치하도록 설정
		when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
		// 토큰 생성과 Bearer 제거 메서드를 모킹
		when(jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname())).thenReturn("Bearer jwt-token");
		when(jwtUtil.subStringToken("Bearer jwt-token")).thenReturn("jwt-token");

		// 로그인 메서드 실행
		SigninResponseDto response = authService.login(signinRequestDto);

		// 반환 토큰이 예상값과 일치하는지 검증하는 부분
		assertEquals("jwt-token", response.getToken());
	}

	@Test
	// 실패 테스트
	void testLogin_invalidPassword() {
		SigninRequestDto signinRequestDto = new SigninRequestDto("test@example.com", "wrong-password");

		// 똑같이 이메일 찾고 비밀번호 비교로 이번엔 false가 나오도록 설정
		when(userRepository.findByEmailOrElseThrow(signinRequestDto.getEmail())).thenReturn(user);
		when(passwordEncoder.matches("wrong-password", user.getPassword())).thenReturn(false);

		// 로그인 시도 시에 예외 발생하는지 검증
		assertThrows(CustomException.class, () -> {
			authService.login(signinRequestDto);
		});
	}
}
