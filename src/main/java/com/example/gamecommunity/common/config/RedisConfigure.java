package com.example.gamecommunity.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfigure {

    @Value("${spring.data.redis.host}")
    private String host;    // host 정보 가져오기

    @Value("${spring.data.redis.port}")
    private int port;   // port 정보 가져오기

    @PostConstruct
    public void printRedisConnection() {
        System.out.println("현재 Redis 연결: " + host + ":" + port);
    }

    // 백엔드, redis 연결 - Lettuce 사용
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        ObjectMapper objectMapper = new ObjectMapper();
        // LocalDateTime 타입이 포함된 객체(Post)를 직렬화해 저장하기 위함
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // objectMapper 를 GenericJackson2JsonRedisSerializer에 적용
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 데이터 저장, 조회를 위한 핵심 객체
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // Redis 서버와의 연결 정보 설정, redisConnectionFactory() -> LettuceConnectionFactory 반환
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        // Redis에 데이터 저장시 어떻게 직렬화할지 지정.
        // key를  문자열(utf-8 바이트 배열)로 직렬화 함
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // GenericJackson2JsonRedisSerializer 사용해 객체를 JSON으로 직렬화해 저장함
        redisTemplate.setValueSerializer(serializer);
        return redisTemplate;

    }
}