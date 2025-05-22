package com.example.gamecommunity.common.auth.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.auth.service.AuthService;
import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.common.enums.SuccessCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/login")
	public CommonResponse<SigninResponseDto> login(@RequestBody SigninRequestDto signinRequestDto){

		SigninResponseDto signinResponseDto = authService.login(signinRequestDto);

		return CommonResponse.of(SuccessCode.SUCCESS_USER_LOGIN, signinResponseDto);
	}

	@DeleteMapping("/logout")
	public CommonResponse<Object> logout(@RequestBody String token){

		authService.logout(token);

		return CommonResponse.of(SuccessCode.SUCCESS_USER_LOGOUT);

	}
}
