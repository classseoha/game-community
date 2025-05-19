package com.example.gamecommunity.common.auth.controller;

import java.time.Duration;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.auth.dto.requestdto.SigninRequestDto;
import com.example.gamecommunity.common.auth.dto.responsedto.SigninResponseDto;
import com.example.gamecommunity.common.auth.service.AuthService;
import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
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

		ResponseCookie cookie = ResponseCookie.from("token", token)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(Duration.ofDays(1))
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok()
			.headers(headers)
			.body(signinResponseDto);
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest httpServletRequest){

		if(httpServletRequest.getCookies() == null) {
			throw new CustomException(ErrorCode.NOT_EXIST_COOKIE);
		}

		ResponseCookie cookie = ResponseCookie.from("token")
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(0)
			.sameSite("Strict")
			.build();

		HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.SET_COOKIE, cookie.toString());

		return ResponseEntity.ok()
			.headers(headers)
			.body("로그아웃 되었습니다.");
	}
}
