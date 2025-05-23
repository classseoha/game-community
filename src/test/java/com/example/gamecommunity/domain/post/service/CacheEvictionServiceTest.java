package com.example.gamecommunity.domain.post.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class CacheEvictionServiceTest {

    @Autowired
    private CacheEvictionService cacheEvictionService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        // 캐시 데이터 삽입
        redisTemplate.opsForValue().set("search:result:test1:page:0", "cached-data-1");
        redisTemplate.opsForValue().set("search:result:test2:page:0", "cached-data-2");
        // 타입 충돌 방지 (정확한 구조로 저장)
        redisTemplate.opsForZSet().add("search:rank:20250523", "롤", 1);

    }

    @Test
    void 검색결과_캐시를_전체삭제한다() {
        // pre-check
        assertThat(redisTemplate.hasKey("search:result:test1:page:0")).isTrue();
        assertThat(redisTemplate.hasKey("search:result:test2:page:0")).isTrue();

        // when
        cacheEvictionService.clearAllSearchCache();

        // then
        assertThat(redisTemplate.hasKey("search:result:test1:page:0")).isFalse();
        assertThat(redisTemplate.hasKey("search:result:test2:page:0")).isFalse();
        assertThat(redisTemplate.hasKey("search:rank:20250523")).isTrue(); // 영향 없어야 함
    }

}
