## core:api 모듈
본 모듈은 Interceptor, Retrofit 객체와 같이 앱 전제적으로 사용되는 네트워크 통신과 관련된 기능이 정의됩니다.

### 의존성 그래프
![core_api모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/0cc09e9d-98ee-412d-b719-5301f28f0830)
- core 레이어에 존재하는 모듈들은 타 모듈을 참조하지 않습니다.

### 세부 패키지 구성
- di : 의존성 주입과 관련된 Qualifier 어노테이션 클래스
- bookmark : 오늘한줄 벡엔드와 https 통신시 사용하는 retrofit 클래스
- google : 구글 소셜로그인시 사용하는 retrofit 객체
- kakao : 카카오 책 조회 api에 사용하는 retrofit 객체 및 interceptor 클래스
- NetworkInterceptor.kt : 기본적으로 사용하는 okhttp interceptor
