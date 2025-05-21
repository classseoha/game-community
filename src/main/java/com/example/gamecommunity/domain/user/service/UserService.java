package com.example.gamecommunity.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.common.util.EntityFetcher;
import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final EntityFetcher entityFetcher;

	@Transactional
	public UserResponseDto registerUser(UserRequestDto userRequestDto) {

		String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());

		User user = User.builder()
			.email(userRequestDto.getEmail())
			.password(encodedPassword)
			.nickname(userRequestDto.getNickname())
			.build();

		User savedUser = userRepository.save(user);

		return UserResponseDto.from(savedUser);
	}

	@Transactional
	public UserResponseDto updateUser(Long userId, UserUpdateRequestDto userUpdateRequestDto) {

		User findUser = entityFetcher.getUserOrThrow(userId);

		if (userUpdateRequestDto.getPassword() != null && !userUpdateRequestDto.getPassword().isEmpty()) {
			String encodedPassword = passwordEncoder.encode(userUpdateRequestDto.getPassword());
			findUser.updatePassword(encodedPassword);
		}

		if (userUpdateRequestDto.getNickname() != null && !userUpdateRequestDto.getNickname().isEmpty()) {
			findUser.updateNickname(userUpdateRequestDto.getNickname());
		}

		return UserResponseDto.from(findUser);
	}

	@Transactional
	public void delete(Long userId) {

		User findUser = entityFetcher.getUserOrThrow(userId);

		userRepository.delete(findUser);
	}
}
