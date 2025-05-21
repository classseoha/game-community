package com.example.gamecommunity.domain.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HotPostRequestDto {
    private String title;
    private String contents;
}
