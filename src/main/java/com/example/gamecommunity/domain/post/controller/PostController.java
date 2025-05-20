package com.example.gamecommunity.domain.post.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.common.enums.SuccessCode;
import com.example.gamecommunity.domain.post.dto.request.PostRequestDto;
import com.example.gamecommunity.domain.post.dto.response.PostResponseDto;
import com.example.gamecommunity.domain.post.service.PostService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	// 1. 게시글 생성
	@PostMapping("/{userId}/posts")
	public CommonResponse<PostResponseDto> createPost(@PathVariable Long userId, @RequestBody @Valid PostRequestDto postRequestDto) {

		return CommonResponse.of(SuccessCode.CREATE_POST_SUCCESS, postService.savePost(userId, postRequestDto));
	}

	// 2. 게시글 목록 조회
	@GetMapping("/{userId}/posts")
	public CommonResponse<List<PostResponseDto>> getPostList(@PathVariable Long userId) {

		return CommonResponse.of(SuccessCode.GET_ALL_POSTS_SUCCESS, postService.getAllPosts(userId));
	}

	// 3. 게시글 검색 조회 v1
	@GetMapping("/v1/posts/search")
	public CommonResponse<Page<PostResponseDto>> v1searchPost(
		@RequestParam("keyword") String title,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

			return CommonResponse.of(SuccessCode.SEARCH_POST_SUCCESS, postService.searchPostByTitle(title, pageable));
	}

	// 4. 게시글 검색 조회 v2 (캐시 기반)
	@GetMapping("/v2/posts/search")
	public CommonResponse<Page<PostResponseDto>> v2searchPostWithCache(
		@RequestParam("keyword") String title,
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

			return CommonResponse.of(SuccessCode.SEARCH_POST_SUCCESS, postService.searchPostByTitleWithCache(title, pageable));
	}

	// 5. 게시글 수정
	@PatchMapping("/posts/{id}")
	public CommonResponse<Void> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto) {

		postService.editPost(id, postRequestDto);

		return CommonResponse.of(SuccessCode.UPDATE_POST_SUCCESS);
	}

	// 6. 게시글 삭제
	@DeleteMapping("/posts/{id}")
	public CommonResponse<Void> deletePost(@PathVariable Long id) {

		postService.deletePost(id);

		return CommonResponse.of(SuccessCode.DELETE_POST_SUCCESS);
	}

}
