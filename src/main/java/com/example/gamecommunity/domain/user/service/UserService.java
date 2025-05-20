package com.example.gamecommunity.domain.user.service;

import org.springframework.stereotype.Service;

import com.example.gamecommunity.domain.user.dto.request.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.response.UserResponseDto;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	// 유저 생성
	@Transactional
	public UserResponseDto createUser(UserRequestDto userRequestDto) {

		User user = User.builder()
			.email(userRequestDto.getEmail())
			.password(userRequestDto.getPassword())
			.nickname(userRequestDto.getNickname())
			.build();

		User savedUser = userRepository.save(user);

		return new UserResponseDto(savedUser.getId(), savedUser.getNickname());
	}
}
