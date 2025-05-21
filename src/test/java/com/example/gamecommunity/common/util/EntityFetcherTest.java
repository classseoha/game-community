package com.example.gamecommunity.common.util;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.domain.post.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostEntityFetcherTest {

	@Mock
	private PostRepository postRepository;

	@InjectMocks
	private EntityFetcher entityFetcher;

	@Test
	void 일치하는_게시글id_없으면_CustomException() {

		// given
		Long id = 1L;

		// stubbing
		when(postRepository.findById(id)).thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> entityFetcher.getPostOrThrow(id))
			.isInstanceOf(CustomException.class)
			.hasMessageContaining("게시글을 찾을 수 없습니다.");

	}

}