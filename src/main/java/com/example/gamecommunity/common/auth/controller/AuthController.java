package com.example.gamecommunity.common.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.auth.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<SigninResponseDto> login(@RequestBody SigninRequestDto signinRequestDto){

		SigninResponseDto signinResponseDto = authService.login(signinRequestDto);

		String token = signinResponseDto.getToken();

		return ResponseEntity.ok(signinResponseDto);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logout(){

		return ResponseEntity.ok("로그아웃 되었습니다.");

	}
}
