## core:datastore 모듈
본 모듈은 앱에서 사용하는 dataStore가 정의된 모듈입니다.

### 의존성 그래프
![core_datastore모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/1fbbd911-9188-4025-a4da-aa878b06175e)
- core 레이어에 존재하는 모듈들은 타 모듈을 참조하지 않습니다.

### 세부 패키지 구성
- proto : proto datastore에 사용할 데이터 형식이 담긴 proto 파일
- datastore : proto에 정의된 데이터 형식을 읽고 쓰는 방식을 정의한 Serializer

### 기타
DataStore는 app 모듈의 di 패키지에서 생성되어 주입됩니다.