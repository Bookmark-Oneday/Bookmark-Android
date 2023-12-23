## Domain 레이어
- domain 레이어의 모듈들은 앱 정보, 책, 파일, 로그인, 오늘한줄, 사용자 정보 도메인에 대한 로직이 정의되어 있습니다.
- domain 레이어의 모듈들은 순수 kotlin 모듈입니다.

### domain 레이어 모듈 내 패키지 구조
domain은 appinfo, book, login, oneline, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- useCase : 도메인과 관련된 useCase 쿨래스
- repository : 도메인 관련 repository 인터페이스
- model : 도메인과 관련된 데이터 클래스
- util : 도메인과 관련된 유틸 함수

### 의존성 그래프
![domain레이어](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/67f28045-8072-4571-84bf-ebb1566658cc)
- domain 레이너 내 모듈들은 core 레이어의 모듈만을 참조합니다.

### 기타
- repository에 정의된 인터페이스는 data 레이어의 모듈에서 구현체가 정의되며, app 모듈의 di 패키지에서 주입됩니다.