package com.example.gamecommunity.common.dummy.config;

import com.example.gamecommunity.domain.post.entity.Post;
import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Random;

@Configuration // 설정 클래스 임을 나타내는 어노테이션
@RequiredArgsConstructor
public class PostInsertJobConfig { // Batch Job에 대해 설정하는 클래스

    private final EntityManagerFactory entityManagerFactory; // JPA 저장을 위해 필요
    private final UserRepository userRepository; // 더미 Post를 만들 때 랜덤 User를 지정하기 위해 사용
    @Qualifier("batchTaskExecutor")
    private final TaskExecutor taskExecutor; // 병렬 실행용

    private static final int TOTAL_COUNT = 1_000_000;
    private static final int CHUNK_SIZE = 1000;

    @Bean
    public Job postInsertJob(JobRepository jobRepository, Step postInsertStep) { // Job 정의 메서드

        return new JobBuilder("postInsertJob", jobRepository) // Job 이름
                .start(postInsertStep) // 단일 Step만 포함
                .build();
    }

    @Bean
    public Step postInsertStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) { // Step 정의 메서드

        return new StepBuilder("postInsertStep", jobRepository) // Step 이름
                .<Post, Post>chunk(CHUNK_SIZE, transactionManager) // chunk : 한 번에 1000개 데이터를 읽고, 처리하고, 저장함
                .reader(postItemReader())
                .writer(postItemWriter())
                .taskExecutor(taskExecutor) // 병렬 처리를 위해 taskExecutor 사용
                .build();
    }

    @Bean
    public ItemReader<Post> postItemReader() { // Reader 정의 메서드

        return new ItemReader<>() { // 1개씩 Post 데이터를 생성해서 반환
            private final Faker faker = new Faker(); // 가짜 제목, 내용 생성
            private final Random random = new Random();
            private int count = 0;
            private final List<User> userList = userRepository.findAll(); // 미리 전체 유저 목록를 가져와서 랜덤으로 Post의 작성자로 지정

            @Override
            public Post read() { // 총 100만 개 까지 생성하도록 제한
                if (count >= TOTAL_COUNT) return null;

                String title = faker.book().title();
                String contents = faker.lorem().paragraph();
                User user = userList.get(random.nextInt(userList.size())); // 랜덤 연결
                count++;

                return new Post(title, contents, user);
            }
        };
    }

    @Bean
    public ItemWriter<Post> postItemWriter() { // Writer 정의 메서드

        return new JpaItemWriterBuilder<Post>() // JpaItemWriter는 읽은 Post 객체를 DB에 저장
                .entityManagerFactory(entityManagerFactory) // entityManagerFactory를 통해 DB와 연결함
                .build();
    }
}
