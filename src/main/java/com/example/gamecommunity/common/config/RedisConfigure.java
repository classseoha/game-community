package com.example.gamecommunity.common.config;

import com.example.gamecommunity.domain.post.dto.response.HotPostResponseDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnection;
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

    @Value("${spring.data.redis.password}")
    private String password;    // 비밀번호 가져오기

    // 백엔드, redis 연결 - Lettuce 사용
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

//    @Bean
//    public ObjectMapper redisObjectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // LocalDateTime 타입이 포함된 객체를 직렬화해 저장하기 위함
//        objectMapper.registerModule(new JavaTimeModule());
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL,
//                JsonTypeInfo.As.PROPERTY
//        );
//        return objectMapper;
//    }

    /**
     * RedisTemplate 설정
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory) {
        // 데이터 저장, 조회를 위한 핵심 객체
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // Redis 서버와의 연결 정보 설정, redisConnectionFactory() -> LettuceConnectionFactory 반환
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // Redis에 데이터 저장시 어떻게 직렬화할지 지정.
        // key를  문자열(utf-8 바이트 배열)로 직렬화 함
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // GenericJackson2JsonRedisSerializer 사용해 객체를 JSON으로 저장함
        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(new StringRedisSerializer()));
        return redisTemplate;
    }

//    /**
//     * RedisTemplate 설정
//     *
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, HotPostResponseDto> redisTemplate(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper) {
//        // objectMapper 를 GenericJackson2JsonRedisSerializer에 적용
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);
//
//        // 데이터 저장, 조회를 위한 핵심 객체
//        RedisTemplate<String, HotPostResponseDto> redisTemplate = new RedisTemplate<>();
//        // Redis 서버와의 연결 정보 설정, redisConnectionFactory() -> LettuceConnectionFactory 반환
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        // Redis에 데이터 저장시 어떻게 직렬화할지 지정.
//        // key를  문자열(utf-8 바이트 배열)로 직렬화 함
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        // GenericJackson2JsonRedisSerializer 사용해 객체를 JSON으로 직렬화해 저장함
//        redisTemplate.setValueSerializer(serializer);
//        return redisTemplate;
//    }
}
