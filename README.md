# 책갈피:오늘한줄 - AOS
<p>
<img width="15%" src="https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/e1a74894-d601-4ba9-ab04-9de6c3dfb3ef"/>
<a href="https://drive.google.com/file/d/1aWV8yaqgbHTXroO-9KpW9BthsctjpKbB/view?usp=drive_link">
  <img width="15%" src="https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/6d8a24c2-1054-45cc-b0e7-d79105a3419b"/>
</a>
</p>
책갈피:오늘한줄은 사용자의 독서 욕구를 증진시키고, 독서 경험을 공유할 수 있도록 하는 독서 전문 커뮤니티 플랫폼입니다.

## 기능
- 바코드 인식을 통한 책 등록
- 독서 기록 타이머
- 달력을 통한 독서 기록 확인
- 책에 대한 한줄평을 남기는 오늘한줄
- 목표 독서시간 설정
- 푸시 알림

## 스크린샷 및 시연 영상
![오늘한줄_example](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/1e17e887-8e44-435f-875a-fa3be4a285b3)

https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/13bc95d2-edc9-4d3d-bd9b-732ec4cce367

## 프로젝트 모듈 구조
![프로젝트구조](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/8444a75d-ced1-4848-b665-e61fc0d0f769)
본 프로젝트는 크게 5가지 레이어로 구성되어 있으며, 각각의 대표 기능은 아래와 같습니다.
- app : application클래스 및 shared preference의 초기화작업, 의존성 주입
- presentation : 화면 및 화면에 사용되는 UI적인 요소
- domain : 앱에서 사용하는 데이터 클래스 및 비스니스 로직
- data : api호출과 같은 네트워크 작업 및 로컬 데이터베이스에 접근하여 데이터를 가져오는 작업을 수행
- core : retrofit, okhttp, room, dataStore 정의, 프로젝트 전체적으로 사용되는 클래스

또한, 라이브러리의 버젼 관리는 gradle/libs.versions.toml 에서 관리하고 있습니다.

## 단일 기능 수행 과정
![단일기능 흐름도](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/eb8b2861-4cd3-4760-ad99-1ddf642d1e2a)
- Data레이어의 Repository 구현체는 Hilt를 사용하여 주입되며, app 모듈의 app.di 패키지에 명시되어 있습니다.
- 위 그래프에서 DataSource를 사용하지 않고 바로 Repository 구현체에서 데이터에 접근하는 경우도 존재합니다.

## 기술 스택

| 분야          | 기술 스택                                           |
|---------------|-----------------------------------------------------|
| **언어**  | `Kotlin`                                           |
| **UI** | `Xml, ViewBinding`                                          |
| **의존성 주입** | `Dagger Hilt`                                          |
| **구조** | `Clean Architecture, MVVM, MVI`              |
| **jetpack** | `CameraX, Navigation`              |
| **데이터베이스** | `Room`              |
| **비동기 처리** | `Flow, Coroutine`              |

## 프로젝트 세부 구조
현재 app과 presentation은 app 모듈에 존재하며 나머지 domain, data, core는 별도의 모듈로 분리되어 있습니다.  
각 모듈 내 readme에 상세한 내용 및 의존성 관계가 명시되어 있습니다.

### 의존성 규칙
1. 모둘의 상호 참조를 방지하기 위해서 동일한 레이어에 배치된 모듈간에는 의존성을 가질 수 없습니다.  
  예시 1) data.book 모듈은 data.user 모듈을 참조할 수 없습니다.
2. domain레이어의 모듈은 core레이어의 모듈만을 참조할 수 있습니다.  
  예시 2_1) domain.book 모듈은 data.book 모듈을 참조할 수 없습니다.  
  예시 2_2) domain.book 모듈은 core.model 모듈을 참조할 수 있습니다.
3. data레이어 모듈은 core레이어 모듈과 data레이어 모듈만을 참조할 수 있습니다.  
  예시 3_1) data.book 모듈은 app 모듈을 참조할 수 없습니다.  
  예시 3_2) data.book 모듈은 domain.book 모듈을 참조할 수 있습니다.
4. app 모듈 내 presentation 패키지는 domain, core레이어 모듈만을 참조할 수 있습니다.  
  예시 4) app 모듈 내 presentation 패키지는 data.book 모듈에 존재하는 클래스 및 함수를 사용할 수 없습니다. 대신 domain.book을 사용해야 합니다.

#### app
- di : 의존성 주입 모듈
- shared_preference : sharedPreference 싱글톤 객체
- DefaultApplication.kt : 어플리케이션 클래스

#### presentation
- adapter : RecyclerView, ViewPager2의 어뎁터
- base : ViewBindingActivity, ViewBindingFragment와 같은 기본 Activity, Fragment 클래스
- screens : 각 화면에 대한 클래스
  - [화면이름]
    - [화면이름]Activity.kt
    - [화면이름]ViewModel.kt
    - [세부화면이름] : 세부 화면(fragment)이 존재하는 경우
      - [세부화면이름]Fragment.kt
      - [세부화면이름]ViewModel.kt
    - component : 해당 화면에서만 사용되는 CustomView
    - model : 해당 화면에서만 사용되는 데이터 클래스
- models : presentation layer에서 공통적으로 사용하는 데이터 클래스
- util : presentation layer에서 공통적으로 사용되는 유틸 함수

#### domain
domain 레이어는 appinfo, book, login, oneline, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- useCase : presentation의 viewModel 클래스에서 사용할 useCase 정의
- repository : data 레이어에서 사용하는 repository의 인터페이스 선언
- model : 앱에서 사용하는 데이터 클래스

#### data
data 레이어는 appinfo, book, google_auth, oneline, token, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- model : DTO, RequestBody, ResponseBody같이 data 레이어에서 사용되는 데이터 클래스 (dto)
- util : data 레이어에서 사용하는 유틸 함수

#### core
core 모듈은 앱 전체에서 공통으로 사용되는 기능들이 배치되어 있습니다.
