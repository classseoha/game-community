package com.example.gamecommunity.domain.post.controller;


import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.domain.post.dto.response.PostSearchResponseDto;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gamecommunity.common.enums.SuccessCode.*;

@RequiredArgsConstructor
@RequestMapping("/v3/posts/search")
@RestController
public class PostSearchController {

    private final PostSearchService postSearchService;

    @GetMapping
    public CommonResponse<List<PostSearchResponseDto>> find(
            @RequestParam String keyword
    ) {
        // 인기 검색어 조회를 위한 키워드 저장
        postSearchService.recodeKeyword(keyword);
        return CommonResponse.of(GET_ALL_POSTS_SUCCESS, postSearchService.find(keyword));
    }

    @GetMapping("/rank")
    public CommonResponse<List<KeywordDto>> getKeywords() {
        System.out.println("topic controller");
        return CommonResponse.of(GET_POPULAR_KEYWORDS_SUCCESS, postSearchService.getKeywords());
    }
}
