package com.example.gamecommunity.domain.post.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {

	@NotBlank(message = "제목을 입력해주세요.")
	private final String title;

	@NotBlank(message = "내용을 입력해주세요.")
	private final String content;

}
