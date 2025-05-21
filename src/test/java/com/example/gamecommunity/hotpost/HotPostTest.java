package com.example.gamecommunity.hotpost;

import com.example.gamecommunity.domain.post.dto.response.HotPostResponseDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.post.service.HotPostService;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class HotPostTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private HotPostService hotPostService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String nickname = "테스트유저_" + UUID.randomUUID();
        User user = User.builder()
                .email("abcd@abc.com")
                .nickname(nickname)
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = new Post("테스트 제목", "내용", user);
        postRepository.save(post);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("캐시에 존재하지 않는 경우 DB 조회 후 Redis에 저장")
    void findPostFromDb() {
        // given
        String keyword = "테스트";
        String cacheKey = "search:result:" + keyword.trim().toLowerCase();

        // when
        List<HotPostResponseDto> responseDto = hotPostService.find(keyword);

        // then
        Object noCache = redisTemplate.opsForValue().get(cacheKey);
        assertThat(noCache).isNull();

        assertThat(responseDto).isNotEmpty();
        assertThat(responseDto.get(0).getTitle()).contains("테스트");

        Object cache = redisTemplate.opsForValue().get(cacheKey);
        assertThat(cache).isNotNull();

    }
}
