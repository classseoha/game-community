package com.example.gamecommunity.common.dummy;

import com.example.gamecommunity.domain.user.entity.User;
import com.example.gamecommunity.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * UserSeeder : 개발용 ID 미리 생성(500건) / ApplicationRunner(빠르고 가벼움) / 앱 시작 시 1회 실행 / 단순,소량 데이터
 * PostInsertJob : 부하 테스트용 대량 데이터 생성(100만 건) / Spring Batch Job(병렬처리, chunk 처리) / 앱 시작 시 or REST,스케줄 트리거 가능 / 대량, 병렬, 트랜잭션 관리 필요
 */
@Component// @DependsOn 실행을 위한 이름 지정
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner { // User 더미 데이터 생성기

    private final UserRepository userRepository;
    private final JobLauncher jobLauncher;
    private final Job postInsertJob;
    private final net.datafaker.Faker faker = new net.datafaker.Faker();

    @Override
    public void run(String... args) throws Exception {
        long currentCount = userRepository.count();
        int targetCount = 500;

        if (currentCount >= targetCount) return;

        int neededCount = targetCount - (int) currentCount;

        Set<String> existingEmails = new HashSet<>(userRepository.findAll()
                .stream()
                .map(User::getEmail)
                .toList());

        Set<String> existingNicknames = new HashSet<>(userRepository.findAll()
                .stream()
                .map(User::getNickname)
                .toList());

        Set<String> generatedEmails = new HashSet<>();
        Set<String> generatedNicknames = new HashSet<>();

        List<User> users = new ArrayList<>();

        while (users.size() < neededCount) {
            String email = faker.internet().emailAddress();
            String password = faker.internet().password();
            String nickname = faker.name().username();

            // 중복 이메일 또는 닉네임이면 skip
            if (existingEmails.contains(email) || generatedEmails.contains(email)) continue;
            if (existingNicknames.contains(nickname) || generatedNicknames.contains(nickname)) continue;

            generatedEmails.add(email);
            generatedNicknames.add(nickname);

            users.add(User.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .build());
        }

        userRepository.saveAll(users);
        System.out.println(neededCount + "명의 User 더미 데이터 생성 완료");

        // 💥 여기서 PostInsertJob을 바로 실행!
        JobParameters params = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("🚀 포스트 배치 작업 실행 시작");
        jobLauncher.run(postInsertJob, params);
    }
}
