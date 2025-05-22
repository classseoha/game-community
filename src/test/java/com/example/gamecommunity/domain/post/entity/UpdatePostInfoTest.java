package com.example.gamecommunity.domain.post.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.example.gamecommunity.common.util.EntityFetcher;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

class UpdatePostInfoTest {

	// Test - 수정 후 re테스트 필요
	@Test
	void 유효값_전달시_필드_업데이트() {

		// given
		String title = "제목";
		String content = "내용";
		User user = User.builder()
			.email("테스트 이메일")
			.password("테스트 비밀번호")
			.nickname("테스트 유저")
			.build();

		Post post = new Post(title, content, user);

		// when
		post.updatePostInfo("수정 제목", "수정 내용");

		// then
		assertThat(post.getTitle()).isEqualTo("수정 제목");
		assertThat(post.getContent()).isEqualTo("수정 내용");
	}

	// Test - 수정 후 re테스트 필요
	@Test
	void 공백_전달시_값_변경_안됨() {

		// given
		String title = "제목";
		String content = "내용";
		User user = User.builder()
			.email("테스트 이메일")
			.password("테스트 비밀번호")
			.nickname("테스트 유저")
			.build();

		Post post = new Post(title, content, user);

		// when
		post.updatePostInfo(" ", " ");

		// then
		assertThat(post.getTitle()).isEqualTo(title);
		assertThat(post.getContent()).isEqualTo(content);
	}

}
