## Data 레이어
- data 레이어의 모듈들은 해당 도메인에 대한 데이터 접근 관련 기능들이 정의되어 있습니다.

### data 레이어 모듈 내 패키지 구조
data 레이어은 appinfo, book, google_auth, oneline, token, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- model : DTO, RequestBody, ResponseBody같이 data 레이어에서 사용되는 데이터 클래스
- util : data 레이어에서 사용하는 유틸 함수

### 의존성 그래프
![data레이어](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/c43adc9d-2a03-4e80-9094-beef79f6dbf6)
- data 레이너 내 모듈들은 domain 레이어의 모듈과 core 레이어의 모듈을 참조합니다.
