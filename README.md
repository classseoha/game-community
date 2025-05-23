# Game Commnunity Project

## 📌 프로젝트 소개

게임 유저들을 위한 커뮤니티 플랫폼으로, 사용자들이 게시글을 작성하고 검색할 수 있는 기능을 제공합니다.<br>
검색 성능을 고려한 인덱스 설계와 Redis 기반 캐시 적용을 고려한 구조로 설계된 웹 애플리케이션입니다.

 
<br>

## 📌 역할 분담

**김하정 (CRUD + 검색)**<br>

- Post 도메인<br>
- 게시판 CRUD API<br>
- 검색 API v1(JPA 기반)/v2(In-memory Cache)<br>
- 페이징 처리 구현
- Indexing

**박한비 (Redis, 인기 검색어)**<br>

- Redis 연동 설정<br>
- 인기 검색어 기능 + Redis Sorted Set 활용<br>
- Remote 캐시 적용
- Cache Eviction 전략 구현

**박서하 (더미 데이터 + 테스트 담당)**<br>

- Faker로 100만 건 이상 더미 데이터 생성<br>
- CommandLineRunner + Batch 처리
- 검색 API 성능 테스트 시나리오 작성
- JMeter 스크립트 작성 및 결과 분석

**석창훈 (CRUD + 인증/인가)**<br>

- User 도메인<br>
- 사용자 CRUD API
- JWT, 시큐리티 적용


<br>

## 📌 ERD

![Image](https://github.com/user-attachments/assets/6050a525-4c58-4b86-9e43-69ab46e4643f)

<br>

## 📌 와이어 프레임

<img width="774" alt="Image" src="https://github.com/user-attachments/assets/cac5b504-9b5b-4986-915c-b4bb10b8721e" />

<br>
<br>

## 📌 API 명세서

* Post
![Image](https://github.com/user-attachments/assets/5c8918e1-0c34-42e9-ae41-9503151904a3)

* User
![Image](https://github.com/user-attachments/assets/ecb77b3f-320e-465c-9809-284182d809c3)
![Image](https://github.com/user-attachments/assets/9a9d1ac9-dbcb-4b3c-bef4-7731990e7263)

* Search (Redis 적용)
![Image](https://github.com/user-attachments/assets/76a72bc2-85e9-430c-bc9d-153fde40907a)

<br>

## 📌 주요 기능

### Post 도메인
- 게시글 작성, 조회, 수정, 삭제 기능 제공
- 게시글 제목 검색 기능 제공 (title LIKE 기반), 검색 결과에 캐시 적용
- `@Cacheable`을 사용하여 제목 검색 시 캐시 자동 적용, 응답 속도 향상
- 게시글 수정 및 삭제는 작성자 본인만 가능하도록 권한 제어<br>
### User 도메인
- 유저 회원가입, 로그인, 회원 정보 수정, 회원 탈퇴 기능
- 회원가입 또는 로그인 시 JWT 토큰을 발급받아 인증 상태 유지 가능
- JWT 토큰 기반으로 사용자 인증 및 게시글 권한 검증 수행<br>
### Redis 캐시
- 자주 검색되는 게시글 검색 결과에 대해 Spring Cache + Redis 적용
- 게시글 생성/수정/삭제 시 관련 캐시를 자동 삭제하여 일관성 유지

<br>

## 📌 Git Commit Message Convention

* Feat : 새로운 기능 추가
* Fix : 버그 수정
* Style : 코드 포맷팅, 코드 오타, 함수명 수정 등 스타일 수정
* Refactor : 코드 리팩토링(동일 기능 내 코드 개선)
* Comment : 주석 수정 및 삭제
* Docs : 문서와 관련된 모든 것
* Chore : 빌드 설정 변경 및 기타 환경설정
 
<br>

## 📌 개발 환경 및 기술 스택

<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> <img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Faker-FF6F61?style=for-the-badge&logoColor=white"> <img src="https://img.shields.io/badge/Batch-0078D6?style=for-the-badge&logo=windows&logoColor=white">

<br>

## 📌 프로젝트 구조

```bas      
📁 OutsourcingProject
            └─ common
                   ├─auth
                   │  └─controller, dto, security, service
                   ├─config
                   ├─dto
                   ├─dummy
                   ├─entity
                   ├─enums
                   ├─exception
                   └─util
            └─ domain
                   ├─post
                   │  └─controller, dto, entity, repository, service
                   └─user
                      └─controller, dto, entity, repository, service
```
<br>
<br>
