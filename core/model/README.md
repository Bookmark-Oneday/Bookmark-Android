## core:model 모듈
본 모듈은 앱에서 공통적으로 사용되는 클래스가 정의된 모듈입니다.

### 의존성 그래프
![core_model모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/0490b545-dd26-4c1c-ae6b-235f7740183c)
- core 레이어에 존재하는 모듈들은 타 모듈을 참조하지 않습니다.

### 세부 파일
- BaseResponse : Api응답 및 일부 repository 메서드 리턴값에 사용되는 BaseResponse 클래스
- PagingCheckData : 페이지네이션 상태를 관리하는 PagingCheckData 클래스
- PagingData : 페이지네이션 리턴값을 나타내는 PagingData 클래스
- TimeString : timestamp 형식의 문자열을 처리하고 다른 클래스 내에서 시간 문자열 변수로 사용되는 TimeString 클래스