package com.example.gamecommunity.domain.post.service;

import com.example.gamecommunity.domain.post.dto.response.CachedPage;
import com.example.gamecommunity.domain.post.dto.response.PostSearchResponseDto;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class PostSearchService {

    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 키워드를 통한 게시글 리스트 반환
     * key : "search:result:{keyword}"
     * value : List<HotPostResponseDto>
     * TTL : 10분
     * Look - Aside 전략 사용
     */
    @Transactional(readOnly = true)
    public CachedPage find(Pageable pageable, String keyword) {
        String getKeyword = getKeyword(keyword);

        String setKey = "search:result:" + getKeyword + ":page:" + pageable.getPageNumber();
        System.out.println("keyword = " + getKeyword);
        System.out.println("redis key =" + setKey);

        Object cached = redisTemplate.opsForValue().get(setKey);
        System.out.println("cached = " + cached);
        System.out.println("Redis 연결 정보: " + redisTemplate.getConnectionFactory().getConnection().ping());

        // 캐시 조회
        if (cached != null) {
            try {
                CachedPage cachedPage = objectMapper.convertValue(
                        cached, new TypeReference<CachedPage>() {
                        }
                );
                return cachedPage;
            } catch (Exception e) {
                System.out.println("error");
            }
        }

        // 캐시에 존재하지 않을 경우 DB 조회
        Page<Post> postList = postRepository.findAllByTitleStartingWith(keyword, pageable);
        List<PostSearchResponseDto> dtoList = postList.stream().map(PostSearchResponseDto::from).toList();

        CachedPage cachedPage = CachedPage.from(dtoList, postList);

        // Redis에 데이터 저장, TTL 설정 - 10분
        redisTemplate.opsForValue().set(setKey, cachedPage, Duration.ofMinutes(5));
        return cachedPage;

    }

    /**
     * 키워드 저장
     * Sorted Set
     * key : "search:rank:20250520"
     * value : "keyword"
     * score : +1
     * TTL : 1일
     */
    @Transactional(readOnly = true)
    public void recodeKeyword(String keyword) {
        // 검색된 키워드
        String getKeyword = getKeyword(keyword);

        // 키워드가 공백이라면 리턴
        if (getKeyword.isBlank()) {
            return;
        }

        // 인기 검색어 저장을 위한 key 생성
        String key = "search:rank:" + LocalDate.now().format(FORMATTER);

        // key가 처음 생성될 경우에만 TTL 설정
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            redisTemplate.expire(key, Duration.ofDays(1));
        }

        // Sorted Set 관련 메서드
        redisTemplate.opsForZSet().incrementScore(key, getKeyword, 1);
    }

    /**
     * 인기 검색어 리스트 반환 (TOP 10)
     * key : "search:rank:20250520"
     * value : "keyword"
     */
    @Transactional(readOnly = true)
    public List<KeywordDto> getKeywords() {
        // 인기 검색어 key
        String key = "search:rank:" + LocalDate.now().format(FORMATTER);

        Set<Object> topKeywordSet = redisTemplate.opsForZSet().reverseRange(key, 0, 9);

        // 키워드가 존재하지 않는 경우 빈 리스트 반환
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
