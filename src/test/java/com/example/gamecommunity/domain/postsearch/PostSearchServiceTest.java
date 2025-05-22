package com.example.gamecommunity.domain.postsearch;

import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.post.service.PostSearchService;
import com.example.gamecommunity.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class PostSearchServiceTest {

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        User user = User.builder()
                .email("abc@abc.com")
                .nickname("유저 이름")
                .password("1234")
                .build();


        Post post = new Post("테스트 제목", "테스트 내용", user);
        postRepository.save(post);
    }
}
