package com.example.gamecommunity.common.dummy;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class BatchStarter implements CommandLineRunner { // CommandLineRunner는 Spring Boot 앱 실행 직후 자동 실행되는 인터페이스 (PostInsertJobConfig + BatchStarter = 고성능 Post Seeder >> 실제 부하 테스트용 대량 seeder)

    private final JobLauncher jobLauncher; // Spring Batch에서 Job을 실행시키는 런처
    private final Job insertPostJob; // Job 빈

    @Override
    public void run(String... args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()) // 매번 Job 실행 시 고유한 JobParameters를 넘겨야 해서 현재 시간 삽입 (이렇게 안하면 "이미 실행된 JobInstance"라는 중복 실행 오류 발생)
                .toJobParameters();

        jobLauncher.run(insertPostJob, params); // reader/writer로 100만 개 insert
    }
}
