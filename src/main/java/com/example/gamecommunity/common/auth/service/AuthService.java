package com.example.gamecommunity.common.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.common.security.JwtUtil;
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

		if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())){
			throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
		}

		String token = jwtUtil.createToken(user.getId(), user.getEmail(), user.getNickname());

		return new SigninResponseDto(jwtUtil.subStringToken(token));
	}
}
