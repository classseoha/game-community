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
     * UNLINK 명령 이용해 키 삭제
     * @param keys 삭제할 키의 집합
     */
    private void unlinkKeys(Set<String> keys) {
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            for (String key : keys) {
                connection.unlink(key.getBytes());
            }
            return null;
        });
    }

    /**
     * SCAN 명령 사용해 키 조회
     * @param keyPattern 조회 키 패턴 (ex) "search:result:*"
     * @return 검색한 키들의 Set<String>
     */
    private Set<String> scanKeys(String keyPattern) {
        // RedisCallback : RedisConnectrion을 직접 제어할 수 있는 low-level API
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            // 결과 저장용 Set
            Set<String> keys = new HashSet<>();

            // 옵션 설정 : 키 패턴, 반환 개수 (기본값 10)
            ScanOptions options = ScanOptions.scanOptions()
                    .match(keyPattern)   // key 패턴
                    .count(10)  // 한번에 가져올 키 수
                    .build();

            // 커서 기반 키 조회
            Cursor<byte[]> cursor = connection.scan(options);   // Cursor: 반복 가능한 객체, Redis의 키는 byte[] 형식

            while (cursor.hasNext()) {
                keys.add(new String(cursor.next())); // 바이트 배열을 문자열로 변환
            }

            return keys;
        });

    }
}
