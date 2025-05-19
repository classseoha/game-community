package com.example.gamecommunity.domain.user.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.domain.user.dto.requestdto.UserRequestDto;
import com.example.gamecommunity.domain.user.dto.requestdto.UserUpdateRequestDto;
import com.example.gamecommunity.domain.user.dto.responsedto.UserResponseDto;
import com.example.gamecommunity.domain.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<UserResponseDto> registerUser(
		@RequestBody UserRequestDto userRequestDto) {

		UserResponseDto userResponseDto = userService.registerUser(userRequestDto);

		return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
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
