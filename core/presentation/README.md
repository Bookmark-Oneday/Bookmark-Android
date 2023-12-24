## core:model 모듈
본 모듈은 presentation 레이어에서 공통적으로 사용되는 기능을 정의한 모듈입니다.

### 의존성 그래프
![core_presentation모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/f4524083-c612-4cee-a0ec-a6b00479b64e)
- core 레이어에 존재하는 모듈들은 타 모듈을 참조하지 않습니다.

### 세부 패키지 구성
- noti : notification 설정, 생성에 사용되는 NotificationManager
- util : Boolean 타입을 visibility 상수값으로 변환하는 유틸 함수
- BaseLoadingLottie.kt : 데이터 로딩시 화면에 표시할 Loading View