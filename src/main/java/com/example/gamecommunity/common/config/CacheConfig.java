package com.example.gamecommunity.common.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {

		/*
		캐시 적용 방법 3가지
		1. ConcurrentMapCacheManager: Spring Boot 에서 기본 제공하는 로컬 메모리 기반 캐시 매니저 → 간단한 테스트용
		2. CaffeineCacheManager: TTL, 용량 제한, 통계, 성능 최적화 등 실무에 적합
		3. Redis: 데이터 저장과 캐싱에 모두 유리한 기능을 제공하는 초고속 인메모리 데이터 저장소
		운영환경에서는 대부분 Caffeine 이나 Redis 를 사용
		 */

		// 2번 Caffeine 사용시
		return new CaffeineCacheManager("searchPost");

		// ↓ 1번으로 했을 경우
		// return new ConcurrentMapCacheManager("searchPost");
	}

}
