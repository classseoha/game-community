package com.example.gamecommunity.domain.postsearch;

import com.example.gamecommunity.domain.post.dto.response.CachedPage;
import com.example.gamecommunity.domain.post.dto.response.KeywordDto;
import com.example.gamecommunity.domain.post.dto.response.PostSearchResponseDto;
import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.post.repository.PostRepository;
import com.example.gamecommunity.domain.post.service.PostSearchService;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
public class PostSearchServiceTest {

    @Autowired
    private PostSearchService postSearchService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    private Post post;
    private User user;

    @BeforeEach
    void setUp() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
        userRepository.deleteAll();
        user = User.builder()
                .email("abc@abc.com")
                .nickname("유저 이름")
                .password("1234")
                .build();
        userRepository.save(user);

        post = new Post("test", "테스트 내용", user);
        postRepository.save(post);
    }

    @Test
    void 캐시가_없으면_DB조회후_캐시에_저장() {
        String keyword = "test";
        Pageable pageable = PageRequest.of(0, 10);
        String expected = "search:result:test:page:0";

        CachedPage result = postSearchService.find(pageable, keyword);

        Object cached = redisTemplate.opsForValue().get(expected);
        assertThat(cached).isNotNull();
        assertThat(result.getContent()).isNotEmpty();

    }

    @Test
    void 캐시가_있으면_역직렬화하여_바로_반환된다() {
        // given
        String keyword = "cacheTest";
        Pageable pageable = PageRequest.of(0, 10);
        String expectedKey = "search:result:cacheTest:page:0";

        post = Post.builder().title("cacheTest").content("내용").user(user).build();
        postRepository.save(post);

        PostSearchResponseDto responseDto = PostSearchResponseDto.from(post);
        List<PostSearchResponseDto> dtoList = List.of(responseDto);
        Page<Post> postPage = new PageImpl<>(List.of(post), pageable, 1);
        CachedPage mockPage = CachedPage.from(dtoList, postPage);

        redisTemplate.opsForValue().set(expectedKey, mockPage);

        // when
        CachedPage result = postSearchService.find(pageable, keyword);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("cacheTest");
    }

    @Test
    void 키워드가_SortedSet에_저장된다() {
        // given
        String keyword = "test";
        String redisKey = "search:rank:" + LocalDate.now().format(FORMATTER);

        // when
        postSearchService.recodeKeyword(keyword);

        // then
        Set<Object> keywords = redisTemplate.opsForZSet().range(redisKey, 0, -1);
        assertThat(keywords).contains("test");
    }

    @Test
    void 인기검색어가_정상적으로_조회된다() {
        // given
        postSearchService.recodeKeyword("test");
        postSearchService.recodeKeyword("test");
        postSearchService.recodeKeyword("배그");

        // when
        List<KeywordDto> keywords = postSearchService.getKeywords();

        // then
        assertThat(keywords).isNotEmpty();
        assertThat(keywords.get(0).getKeyword()).isEqualTo("test");
    }
}
