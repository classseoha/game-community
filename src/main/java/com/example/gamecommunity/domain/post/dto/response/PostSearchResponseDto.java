package com.example.gamecommunity.domain.post.dto.response;

import com.example.gamecommunity.domain.post.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostSearchResponseDto {

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostSearchResponseDto(String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PostSearchResponseDto from(Post post) {
        return new PostSearchResponseDto(
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
