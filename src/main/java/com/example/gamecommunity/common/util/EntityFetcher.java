package com.example.gamecommunity.common.util;

import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import org.springframework.stereotype.Component;

import com.example.gamecommunity.common.enums.ErrorCode;
import com.example.gamecommunity.common.exception.CustomException;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityFetcher {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // User
    public User getUserOrThrow(Long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    // Post
    public Post getPostOrThrow(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
    }

}
