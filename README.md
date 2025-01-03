# 플러스 주차 개인과제

# 사용 스택
- JAVA: JDK 17
- Spring Boot: 3.3.5
- MySQL: Ver 8+
- Gradle: 빌드 및 의존성 관리
- IntelliJ IDEA: 통합 개발 환경(IDE)
- Lombok: 코드 간소화

# 구현 단계
### Transactional 에 대한 이해
- @Transactional annotation 사용
### 인가에 대한 이해
- UserFilter 구현
- WebConfig 적용
### N + 1 에 대한 이해
- EntityGraph 사용
### DB 접근 최소화
- findByIdIn 사용
- JPQL UPDATE -- IN -- 사용
### 동적 쿼리에 대한 이해
- queryDSL 사용
### 필요한 부분만 갱신하기
- @DynamicInsert 적용
- entity 필드 default 삭제
### 리팩토링
- early return 으로 else 삭제
- 응답 데이터 타입 생성 후 반환
- findById default 메서드로 변경 후 적용
- 상태값 Enum 관리
### 테스트코드
- PasswordEncoder 단위 테스트 적용
- nullable = false 속성은 ItemRepositoryTest 에서 수행
### 테스트코드 (도전)
- 모든 컨트롤러 단위 테스트 추가
- 모든 서비스 단위 테스트 추가
- Reservation Service 통합 테스트 추가
- Jacoco 테스트 커버리지 100% 달성
  ![image](https://github.com/user-attachments/assets/8d26f41e-8530-4ff8-93ac-5be3be01e238)
### 테스트 환경 분리
- 인메모리 H2 DB 사용
- data.sql 사용해 초기 데이터 세팅

# 트러블 슈팅
- [Controller Test](https://velog.io/@alsqja2626/Spring-Controller-Test)
- [통합 테스트와 단위 테스트 패키징 방법](https://velog.io/@alsqja2626/Spring-%ED%86%B5%ED%95%A9-%ED%85%8C%EC%8A%A4%ED%8A%B8%EC%99%80-%EB%8B%A8%EC%9C%84-%ED%85%8C%EC%8A%A4%ED%8A%B8-%ED%8C%A8%ED%82%A4%EC%A7%95-%EB%B0%A9%EB%B2%95)
- [End-to-End 와 통합 테스트](https://velog.io/@alsqja2626/Spring-End-to-End-%EC%99%80-%ED%86%B5%ED%95%A9%EC%84%9C%EB%B9%84%EC%8A%A4)
- [Test Code](https://velog.io/@alsqja2626/Spring-Test-Code)

