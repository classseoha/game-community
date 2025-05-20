package com.example.gamecommunity.domain.user.controller;

import java.time.Duration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.security.JwtUtil;
import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserAuthResponseDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import com.example.gamecommunity.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<UserAuthResponseDto> registerUser(
		@RequestBody UserRequestDto userRequestDto,
		HttpServletResponse response) {

		UserResponseDto userResponseDto = userService.registerUser(userRequestDto);

		User savedUser = userRepository.findByEmailOrElseThrow(userRequestDto.getEmail());
		String token = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), savedUser.getNickname());

		UserAuthResponseDto userAuthResponse = new UserAuthResponseDto(
			savedUser.getEmail(),
			savedUser.getNickname(),
			token
		);

	// 	ResponseCookie cookie = ResponseCookie.from("token", token)
	// 		.httpOnly(true)
	// 		.secure(true)
	// 		.path("/")
	// 		.maxAge(Duration.ofDays(1))
	// 		.sameSite("Strict")
	// 		.build();
	//
	// response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

		return new ResponseEntity<>(userAuthResponse, HttpStatus.CREATED);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserResponseDto> updateuser(
		@PathVariable Long id, @RequestBody UserUpdateRequestDto userUpdateRequestDto) {

		UserResponseDto userResponseDto = userService.updateUser(id, userUpdateRequestDto);

		return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(Long id) {

		userService.delete(id);

		return new ResponseEntity<>(HttpStatus.OK);
	}
}
