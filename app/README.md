## App 모듈
본 모듈은 app, presentation 패키지로 구성되어 있으며, 각 패키지가 수행하는 역할은 아래와 같습니다.  
- app : application클래스 및 shared preference의 초기화작업, 의존성 주입
- presentation : 화면 및 화면에 사용되는 UI적인 요소

### 의존성 그래프
![app모듈](https://github.com/Bookmark-Oneday/Bookmark-Android/assets/39579912/aee901cc-a6e0-4d0b-a80f-d8b09c3bf096)
- app 모듈 자체는 본 프로젝트를 구성하는 모든 모듈을 참조하고 있으며 이는 Application 클래스에서 수행하는 초기화작업 및 의존성 주입을 위함입니다.
- presentation 패키지에서는 domain 레이어의 모듈들만을 참조하고 있습니다.

### 세부 패키지 구성
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
