package com.example.gamecommunity.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CacheEvictionService {

    /**
     * Redis 접근 템플릿
     */
    private final RedisTemplate<String, Object> redisTemplate;


    /**
     * 게시글 검색 결과 캐시 전체 삭제
     * key : "search:result:*"
     */
    public void clearAllSearchCache() {
        String keyPattern = "search:result:*";
        Set<String> keys = scanKeys(keyPattern);    // scan으로 key 수집
        unlinkKeys(keys);   // unlink로 삭제처리
    }

    /**
     * unlink 명령 이용해 키 삭제
     */
    private void unlinkKeys(Set<String> keys) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            for (String key : keys) {
                connection.unlink(key.getBytes());
            }
            return null;
        });
    }

    private Set<String> scanKeys(String keyPattern) {
        // RedisCallback : RedisConnectrion을 직접 제어할 수 있는 low-level API
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keys = new HashSet<>();

            ScanOptions options = ScanOptions.scanOptions()
                    .match(keyPattern)   // key 패턴
                    .count(10)  // 기본값. 한번에 가져올 키 수
                    .build();

            Cursor<byte[]> cursor = connection.scan(options);   // Cursor: 반복 가능한 객체, Redis의 키는 byte[] 형식

            while (cursor.hasNext()) {
                keys.add(new String(cursor.next())); // 바이트 배열을 문자열로 변환
            }

            return keys;
        });

    }
}
