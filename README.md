# Game Commnunity Project

## 📌 프로젝트 소개
게임 유저들을 위한 커뮤니티 플랫폼으로, 사용자들이 게시글을 작성하고 검색할 수 있는 기능을 제공합니다.
검색 성능을 고려한 인덱스 설계와 Redis 기반 캐시 적용을 고려한 구조로 설계된 웹 애플리케이션입니다.


<br>

## 📌 역할 분담
**김하정 (CRUD + 검색)**<br>

- Post 도메인<br>
- 게시판 CRUD API<br>
- 검색 API v1(JPA 기반)/v2(In-memory Cache)<br>
- 페이징 처리 구현

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

* Redis
![Image](https://github.com/user-attachments/assets/76a72bc2-85e9-430c-bc9d-153fde40907a)

<br>

## 📌 주요 기능
### 유저 도메인
- 유저 회원가입, 로그인, 회원 조회, 회원정보 수정, 회원 탈퇴
- 회원가입 api를 자동 로그인으로 구현하여, 회원가입 또는 로그인 api를 실행하면 JWT 토큰을 발급 받아 로그인 상태를 유지할 수 있습니다.<br>
### 가게 도메인
- 가게 생성, 가게 조회, 가게 수정, 가게 삭제
- JWT에서 로그인한 유저의 권한(일반 사용자 권한, 가게 운영자 권한)을 추출하여, 가게 운영자 권한을 가진 유저만 가게 생성, 가게 수정, 가게 삭제를 할 수 있습니다.<br>
### 메뉴 도메인
- 메뉴 생성, 메뉴 조회, 메뉴 수정, 메뉴 삭제
- JWT에서 로그인한 유저의 권한(일반 사용자 권한, 가게 운영자 권한)을 추출하여, 가게 운영자 권한을 가진 유저만 메뉴 생성, 메뉴 수정, 메뉴 삭제를 할 수 있습니다.<br>
### 주문 도메인
- 주문 요청, 주문 조회, 주문 상태 변경, 주문 삭제
- JWT에서 로그인한 유저의 권한(일반 사용자 권한, 가게 운영자 권한)을 추출하여, 가게 운영자 권한을 가진 유저는 주문 조회, 주문 수정, 주문 삭제를 할 수 있습니다.
- 일반 사용자 권한을 가진 유저는 주문 요청, 주문 조회, 주문 삭제를 할 수 있습니다.<br>
### 리뷰 도메인
- 리뷰 생성, 리뷰 답글(사장님 권한), 리뷰 조회, 리뷰 삭제
- JWT에서 로그인한 유저의 권한(일반 사용자 권한, 가게 운영자 권한)을 추출하여, 일반 사용자 권한을 가진 유저 중에 주문을 생성한 유저만 리뷰 생성, 리뷰 수정, 리뷰 삭제를 할 수 있습니다.
- 가게 운영자 권한을 가진 유저만 주문 조회, 주문 수정, 주문 삭제를 할 수 있습니다.<br>

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
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> 

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
                   │  └─config
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



-----
-----
 향후 Elasticsearch 확장성



