package com.example.gamecommunity.domain.post.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.user.entity.User;

public interface PostRepository extends JpaRepository<Post, Long> {

	Optional<Post> findPostById(Long id);

	List<Post> findAllByUser(User user);

	/*
	페이징 적용하는 방법:
	1. Containing 기반 JPA
	2. QueryDSL
	→ 이번 프로젝트는 제목 검색으로 구현하기로 했기에 간단하게 구현 가능한 JPA 활용
	→ 여러 조건, 내용 + 제목 검색, 정렬 조건 다양한 경우에는 QueryDSL 사용 추천
	 */

	// Page<Post> findAllByTitleContaining(String title, Pageable pageable);
	// Index 는 접두사 검색일 때만 B-Tree 인덱스 활용하기 때문에 Containing → StartingWith 메서드명 수정
	Page<Post> findAllByTitleStartingWith(String title, Pageable pageable);

	// Page<Post> findAllByTitleContaining(keyword);
}
