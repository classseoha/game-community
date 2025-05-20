package com.example.gamecommunity.domain.post.controller;


import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.domain.post.dto.response.HotPostResponseDto;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.service.HotPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gamecommunity.common.enums.SuccessCode.*;

@RequiredArgsConstructor
@RequestMapping("/hotposts")
@RestController
public class HotPostController {

    private final HotPostService hotPostService;

    @GetMapping
    public CommonResponse<List<HotPostResponseDto>> find(
            @RequestParam String keyword
    ) {
        // 인기 검색어 조회를 위한 키워드 저장
        hotPostService.recodeKeyword(keyword);
        return CommonResponse.of(GET_ALL_POSTS_SUCCESS, hotPostService.find(keyword));
    }

    @GetMapping("/toptopic")
    public CommonResponse<List<KeywordDto>> getKeywords() {

        return CommonResponse.of(GET_POPULAR_KEYWORDS_SUCCESS, hotPostService.getKeywords());
    }
}
