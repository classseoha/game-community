package com.example.gamecommunity.domain.post.dto.response;

import com.example.gamecommunity.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HotPostResponseDto {

    private String title;
    private String contents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HotPostResponseDto(String title, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static HotPostResponseDto from(Post post) {
        return new HotPostResponseDto(
                post.getTitle(),
                post.getContents(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
