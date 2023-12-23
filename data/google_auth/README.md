## data:google_auth 모듈
- 구글 소셜로그인에 대한 데이터 접근 관련 기능들이 정의되어 있습니다.

### data:google_auth 모듈 내 패키지 구조
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- model : RequestBody, ResponseBody같이 data 레이어에서 사용되는 데이터 클래스
- util : data 레이어에서 사용하는 유틸 함수

### 의존성 그래프
![data_google_auth모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/40457728-77a9-4bf9-9c8b-b9e72b3877c8)
