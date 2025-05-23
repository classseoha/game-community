package com.example.gamecommunity.domain.post.controller;


import com.example.gamecommunity.common.dto.CommonResponse;
import com.example.gamecommunity.domain.post.dto.response.CachedPage;
import com.example.gamecommunity.domain.post.dto.response.PostSearchResponseDto;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.service.PostSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.gamecommunity.common.enums.SuccessCode.*;

@RequiredArgsConstructor
@RequestMapping("/v3/posts/search")
@RestController
public class PostSearchController {

    private final PostSearchService postSearchService;

    /**
     * 키워드로 게시글 리스트 검색
     * 페이징 적용
     * Redis 적용
     * @param keyword String = "keyword"
     * @param pageable
     * @return
     */
    @GetMapping
    public CommonResponse<CachedPage> find(
            @RequestParam String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        // 인기 검색어 조회를 위한 키워드 저장
        postSearchService.recodeKeyword(keyword);
        return CommonResponse.of(GET_ALL_POSTS_SUCCESS, postSearchService.find(pageable, keyword));
    }

    /**
     * 인기 검색어 TOP 10 조회
     * @return
     */
    @GetMapping("/rank")
    public CommonResponse<List<KeywordDto>> getKeywords() {
        return CommonResponse.of(GET_POPULAR_KEYWORDS_SUCCESS, postSearchService.getKeywords());
    }
}
