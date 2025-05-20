package com.example.gamecommunity.domain.post.dto.response;

import java.time.LocalDateTime;

import com.example.gamecommunity.domain.post.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {

	private final Long id;
	private final String title;
	private final String content;
	private final String nickname;
	private final LocalDateTime createdAt;
	private final LocalDateTime updatedAt;

	public PostResponseDto(Post post) {

		this.id = post.getId();
		this.title = post.getTitle();
		this.content = post.getContent();
		this.nickname = post.getUser().getNickname();
		this.createdAt = post.getCreatedAt();
		this.updatedAt = post.getUpdatedAt();
	}

}
