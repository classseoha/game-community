package com.example.gamecommunity.domain.post.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.common.enums.SuccessCode;
import com.example.gamecommunity.domain.post.dto.request.PostRequestDto;
import com.example.gamecommunity.domain.post.dto.response.PostResponseDto;
import com.example.gamecommunity.domain.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	// 1. 게시글 생성
	// @PostMapping("/posts")
	// public CommonResponse<PostResponseDto> createPost(@RequestBody @Valid PostRequestDto postRequestDto) {
	//
	// 	return CommonResponse.of(SuccessCode.CREATE_POST_SUCCESS, postService.savePost(postRequestDto));
	// }

	// 2. 게시글 목록 조회

	// 3. 게시글 수정


	// 4. 게시글 삭제






}
