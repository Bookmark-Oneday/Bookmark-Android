## domain:book 모듈
도서 목록 조회, 책 등록, 독서 기록과 같이 책과 관련된 도메인에 대한 로직이 정의된 모듈입니다.

### 의존성 그래프
![domain_book모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/e776340e-846f-42ce-8160-0aebfd030564)

### 세부 패키지 구성
- useCase : 도서 관련된 useCase 쿨래스
- repository : 도서 관련 repository 인터페이스
- model : 도서 관련된 데이터 클래스
- util : 도서 관련된 유틸 함수