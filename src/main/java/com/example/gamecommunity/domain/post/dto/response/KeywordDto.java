package com.example.gamecommunity.domain.post.dto.response;

import lombok.Getter;

@Getter
public class KeywordDto {
    private int rank;
    private String keyword;

    public KeywordDto(int rank, String keyword) {
        this.rank = rank;
        this.keyword = keyword;
    }

}
