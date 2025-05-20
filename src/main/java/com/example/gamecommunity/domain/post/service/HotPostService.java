package com.example.gamecommunity.domain.post.service;

import com.example.gamecommunity.common.util.EntityFetcher;
import com.example.gamecommunity.domain.post.dto.request.HotPostRequestDto;
import com.example.gamecommunity.domain.post.dto.response.HotPostResponseDto;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HotPostService {

    private final PostRepository postRepository;
    private final EntityFetcher entityFetcher;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Transactional
    public HotPostResponseDto save(Long userId, HotPostRequestDto requestDto) {
        User user = entityFetcher.getUserOrThrow(userId);
        Post post = new Post(requestDto.getTitle(), requestDto.getContents(), user);
        Post savedPost = postRepository.save(post);
        return HotPostResponseDto.from(savedPost);
    }

    /**
     * 키워드를 통한 게시글 리스트 반환
     * key : "search:result:keyword"
     * value : List<Post>
     * TTL : 10분
     * Look - Aside 전략 사용
     */
    public List<HotPostResponseDto> find(String keyword) {
        keyword = getKeyword(keyword);
        String key = "search:result:" + keyword;

        Object cached = redisTemplate.opsForValue().get(key);

        // 캐시 조회
        if (cached != null) {
            List<HotPostResponseDto> cachedPostList = (List<HotPostResponseDto>) cached;
            return cachedPostList;
        }

        // 캐시에 존재하지 않을 경우 DB 조회
        List<Post> postList = postRepository.findByTitleContaining(keyword);
        List<HotPostResponseDto> dtoList = postList.stream().map(HotPostResponseDto::from).toList();

        // Redis에 데이터 저장, TTL 설정 - 10분
        redisTemplate.opsForValue().set(key, dtoList, Duration.ofMinutes(10));
        return dtoList;

    }

    /**
     * 키워드 저장
     * Sorted Set
     * key : "search:rank:20250520"
     * value : "keyword"
     * score : +1
     * TTL : 1일
     */
    public void recodeKeyword(String keyword) {
        keyword = getKeyword(keyword);
        String key = "search:rank:" + LocalDate.now().format(FORMATTER);

        // key가 처음 생성될 경우에만 TTL 설정
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }

        redisTemplate.opsForZSet().incrementScore(key, keyword, 1);
    }

    /**
     * 인기 검색어 리스트 반환 (TOP 10)
     * key : "search:rank:20250520"
     * value : "keyword"
     *
     * @return
     */
    public List<KeywordDto> getKeywords() {
        String key = "search:rank:" + LocalDate.now().format(FORMATTER);
        Set<Object> topKeywordSet = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        // 키워드가 존재하지 않는 경우
        if (topKeywordSet == null || topKeywordSet.isEmpty()) {
            return List.of();
        }
        List<KeywordDto> keywordList = new ArrayList<>();

        // 1 ~ 10 순위
        int rank = 1;

        for (Object keywordObj : topKeywordSet) {
            String keyword = keywordObj.toString();
            keywordList.add(new KeywordDto(rank++, keyword));
        }

        return keywordList;
    }

    // 키워드 공백 제거, 대/소문자 구분 제거
    private String getKeyword(String keyword) {
        return keyword.trim().toLowerCase();
    }
}
