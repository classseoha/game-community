package com.example.gamecommunity.domain.post.service;

import org.springframework.stereotype.Service;

import com.example.gamecommunity.common.util.EntityFetcher;
import com.example.gamecommunity.domain.post.dto.request.PostRequestDto;
import com.example.gamecommunity.domain.post.dto.response.PostResponseDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final EntityFetcher entityFetcher;
	private final PostRepository postRepository;

	// 1. 게시글 생성
	@Transactional
	public PostResponseDto savePost(String token, PostRequestDto postRequestDto) {

		Long userId = jwtUtil.getUserIdFromToken(token);

		User user = entityFetcher.getUserOrThrow(userId);

		Post post = Post.builder()
			.title(postRequestDto.getTitle())
			.content(postRequestDto.getContent())
			.user(user)
			.build();

		postRepository.save(post);

		return new PostResponseDto(post);

	}

	// 2. 게시글 목록 조회

	// 3. 게시글 수정
	@Transactional
	public void editPost(Long postId, String token, PostRequestDto postRequestDto) {

		Post post = entityFetcher.getPostOrThrow(postId);

		post.update(postRequestDto.getTitle(), postRequestDto.getContent());
	}

	// 4. 게시글 삭제
	@Transactional
	public void deletePost(Long postId, String token) {

		Post post = entityFetcher.getPostOrThrow(postId);

		postRepository.delete(post);
	}

}
