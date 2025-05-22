package com.example.gamecommunity.common.dummy.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableBatchProcessing // Spring Batch 설정을 활성화하는 어노테이션 (기본적인 JobRepository, JobLauncher 등을 자동으로 등록해줌)
public class BatchConfig { // Spring Batch 기본 설정하는 클래스

    @Bean(name = "batchTaskExecutor")
    public TaskExecutor taskExecutor() { // 병렬 처리를 위한 스레드풀 설정 (Step 내에서 taskExecutor()를 지정해주면 여러 청크를 동시에 처리할 수 있음)

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("batch-thread-");
        executor.initialize();
        return executor;
    }
}
