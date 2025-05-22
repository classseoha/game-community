package com.example.gamecommunity.common.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.common.auth.security.JwtUtil;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public SigninResponseDto login(SigninRequestDto signinRequestDto){

		User user = userRepository.findByEmailOrElseThrow(signinRequestDto.getEmail());

		// 클라이언트가 보낸 비밀번호와 DB에 저장된 해시 비밀번호 비교
		if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())){
			throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
		}

		// 사용자의 ID, 이메일, 닉네임 기반으로 토큰 생성
		String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname());

		// 토큰을 Bearer 접두사 제거하는 메서드 적용 후 토큰 반환
		return new SigninResponseDto(jwtUtil.subStringToken(token));
	}

	// public void logout(String token){
	// 	String tokenKey = "BLACKLIST_" + token;
	// 	redisTemplate.opsForValue().set(tokenKey, "true", tokenExpireTime, TimeUnit.MILLISECONDS);
	// }
}
