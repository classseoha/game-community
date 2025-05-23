package com.example.gamecommunity.domain.post.entity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.gamecommunity.domain.user.entity.User;

class UpdatePostInfoTest {

	@Test
	void 유효값_전달시_필드_업데이트() {

		// given
		Post post = new Post("제목", "내용", createTestUser());

		// when
		post.updatePostInfo("수정 제목", "수정 내용");

		// then
		assertThat(post.getTitle()).isEqualTo("수정 제목");
		assertThat(post.getContent()).isEqualTo("수정 내용");
	}

	/* TODO
	Test failed - 수정 필요
	 */
	@Test
	void 공백_전달시_값_변경_안됨() {

		// given
		String originalTitle = "제목";
		String originalContent = "내용";
		Post post = new Post(originalTitle, originalContent, createTestUser());

		// when
		post.updatePostInfo(" ", " ");

		// then
		assertThat(post.getTitle()).isEqualTo(originalTitle);
		assertThat(post.getContent()).isEqualTo(originalContent);
	}

	// 테스트용 사용자 생성 메서드
	private User createTestUser() {
		return User.builder()
			.email("테스트 이메일")
			.password("테스트 비밀번호")
			.nickname("테스트 유저")
			.build();
	}
}
