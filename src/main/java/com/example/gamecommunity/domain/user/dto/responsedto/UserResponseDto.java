package com.example.gamecommunity.domain.user.dto.responsedto;

import java.time.LocalDateTime;

import com.example.gamecommunity.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

	private final Long id;

	private final String email;

	private final String nickname;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	private final LocalDateTime updatedAt;

	@Builder
	public UserResponseDto(Long id, String email, String nickname, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		this.id = id;
		this.email = email;
		this.nickname = nickname;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public static UserResponseDto from(User user) {
		return UserResponseDto.builder()
			.id(user.getId())
			.email(user.getEmail())
			.nickname(user.getNickname())
			.createdAt(user.getCreatedAt())
			.updatedAt(user.getUpdatedAt())
			.build();
	}

}
