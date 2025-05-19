package com.example.gamecommunity.domain.post.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.gamecommunity.common.util.EntityFetcher;
import com.example.gamecommunity.domain.post.dto.request.PostRequestDto;
import com.example.gamecommunity.domain.post.dto.response.PostResponseDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.user.entity.User;

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
	@Transactional(readOnly = true)
	public List<PostResponseDto> getAllPosts(Long postId) {

		List<Post> postList = postRepository.findAllById(postId);

		List<PostResponseDto> postResponseDtoList = postList.stream()
			.map(post -> new PostResponseDto(post))
			.toList();

		return postResponseDtoList;
	}

	// 3. 게시글 검색 조회
	@Transactional(readOnly = true)
	public Page<PostResponseDto> searchPostByTitle(String title, Pageable pageable) {

		return postRepository.findAllByTitleContaining(title, pageable)
			.map(PostResponseDto::new);
	}

	// 4. 캐시 기반 게시글 검색 조회
	@Transactional(readOnly = true)
	@Cacheable(value = "searchPost", key = "#title")
	/*
	검색할 때 세부 설정 넣는 방법
	@Cacheable(value = "searchPosts", key = "#title + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
	 */
	public Page<PostResponseDto> searchPostByTitleWithCache(String title, Pageable pageable) {

		return postRepository.findAllByTitleContaining(title, pageable)
			.map(PostResponseDto::new);
	}

	// 4. 게시글 수정
	@Transactional
	public void editPost(Long postId, String token, PostRequestDto postRequestDto) {

		Post post = entityFetcher.getPostOrThrow(postId);

		post.update(postRequestDto.getTitle(), postRequestDto.getContent());
	}

	// 5. 게시글 삭제
	@Transactional
	public void deletePost(Long postId, String token) {

		Post post = entityFetcher.getPostOrThrow(postId);

		postRepository.delete(post);
	}

}
