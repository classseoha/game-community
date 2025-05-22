package com.example.gamecommunity.usertest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import com.example.gamecommunity.domain.user.service.UserService;
import com.example.gamecommunity.common.util.EntityFetcher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserServiceTest {

	// 의존성들 모킹
	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private EntityFetcher entityFetcher;

	// 위에서 만든 mock들을 UserService에 자동 주입하여 테스트 가능하게 설정
	@InjectMocks
	private UserService userService;

	// 테스트에 사용할 사용자 객체를 위한 필드
	private UserRequestDto userRequestDto;
	private User user;

	@BeforeEach // 각 테스트 메소드 실행 전에 실행되는 메소드 지정
	void setUp() {
		// 위에 설정한 어노테이션(@Mock, @InjectMocks)이 실제 동작하도록 초기화
		MockitoAnnotations.openMocks(this);

		// Dto 생성과 Builder 패턴을 통해 객체 생성
		userRequestDto = new UserRequestDto("test@example.com", "password", "test-nickname");
		user = User.builder()
			.email("test@example.com")
			.password("encoded-password")
			.nickname("test-nickname")
			.build();
	}

	@Test
	void testRegisterUser() {
		// 이메일 중복이 없다고 설정
		when(userRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
		// 닉네임 중복도 없다고 설정
		when(userRepository.existsByNickname(userRequestDto.getNickname())).thenReturn(false);
		// 비밀번호 해시한 결과는 항상 "encoded-password"로 반환하도록 설정
		when(passwordEncoder.encode(userRequestDto.getPassword())).thenReturn("encoded-password");
		// save가 호출되면 미리 만든 user 객체를 반환하도록 설정
		when(userRepository.save(any(User.class))).thenReturn(user);

		// 등록 메서드 실행
		UserResponseDto response = userService.registerUser(userRequestDto);

		// 응답 Dto의 필드값이 기대한 값과 같은지 확인
		assertEquals("test@example.com", response.getEmail());
		assertEquals("test-nickname", response.getNickname());
	}
}
