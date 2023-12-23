## data:oneline 모듈
- 책에 대한 한줄감상인 오늘한줄에 대한 데이터 접근 관련 기능들이 정의되어 있습니다.

### data:oneline 모듈 내 패키지 구조
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- model : DTO, RequestBody, ResponseBody같이 data 레이어에서 사용되는 데이터 클래스

### 의존성 그래프
![data_oneline모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/f6672e3b-c8a9-415c-9e1e-39ff50edbd5d)
