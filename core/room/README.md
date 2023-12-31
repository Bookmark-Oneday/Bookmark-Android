## core:room 모듈
본 모듈은 앱에서 사용되는 로컬 데이터베이스인 room 관련 클래스 및 dto를 정의한 모듈입니다.

### 의존성 그래프
![core_room모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/8d640089-b765-4974-b038-6d7dd0ebbf35)
- core 레이어에 존재하는 모듈들은 타 모듈을 참조하지 않습니다.

### 세부 패키지 구성
- converter : list 타입을 entity에 저장할 떄 사용되는 gson converter
- dao : room에 사용되는 Data Access Object
- database : Room Database
- entity : Room Database의 entity
- model : Room Database에서 데이터를 리턴할 때 사용하는 Data Transfer Object