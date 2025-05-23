package com.example.gamecommunity.common.dummy;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class BatchJobLogger implements JobExecutionListener {

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println("배치 시작");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long endTime = System.currentTimeMillis();
        System.out.println("배치 종료 - 소요 시간: " + (endTime - startTime) / 1000 + "초");
    }
}
