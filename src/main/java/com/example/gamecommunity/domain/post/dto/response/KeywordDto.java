package com.example.gamecommunity.domain.post.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeywordDto {
    private int rank;
    private String keyword;

    public KeywordDto(int rank, String keyword) {
        this.rank = rank;
        this.keyword = keyword;
    }

}
