package com.example.gamecommunity.redis;

import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.user.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    void reset() {
        redisTemplate.delete("test:post");
    }


    @Test
    void redis에_객체_저장() {
        //given
        User user = User.builder()
                .email("abc@abc.com")
                .password("1234")
                .nickname("테스트")
                .build();

        Post post = new Post("제목", "내용", user);

        // when
        redisTemplate.opsForValue().set("test:post",post);
        // redis에서 "test:post" 키로 값 조회
        Object result = redisTemplate.opsForValue().get("test:post");

        //then
        assertThat(result).isInstanceOf(Post.class);   // 반환 타입 확인
        Post savedPost = (Post) result; // 반환값이 Object로 간주되므로 명시적인 형 변환 필요
        assertThat(savedPost.getTitle()).isEqualTo("제목");
        assertThat(savedPost.getContents()).isEqualTo("내용");

    }
}
