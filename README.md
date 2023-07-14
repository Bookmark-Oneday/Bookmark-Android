# 책갈피:오늘한줄 - AOS

## 프로젝트 구조
본 프로젝트는 크게 5가지 분류로 구성되어 있으며, 각각의 대표 기능은 아래와 같습니다.
- app : application클래스 및 shared preference의 초기화작업, 의존성 주입
- presentation : 화면 및 화면에 사용되는 UI적인 요소
- domain : 앱에서 사용하는 데이터 클래스 및 비스니스 로직
- data : api호출과 같은 네트워크 작업 및 로컬 데이터베이스에 접근하여 데이터를 가져오는 작업을 수행
- core : retrofit, okhttp, room, dataStore 정의, 프로젝트 전체적으로 사용되는 클래스

## 폴더 상세 구조
현재 app과 presentation은 app 모듈에 존재하며 나머지 domain, data, core는 별도의 모듈로 분리되어 있습니다.

### 주의사항
1. 모둘의 상호 참조를 방지하기 위해서 동일한 layer에 배치된 모듈간에는 의존성을 가질 수 없습니다.
  예시 1) data.book 모듈은 data.user 모듈을 참조할 수 없습니다.
2. domain 모듈은 core 모듈만을 참조할 수 있습니다.
  예시 2_1) domain.book 모듈은 data.book 모듈을 참조할 수 없습니다.
  예시 2_2) domain.book 모듈은 core.model 모듈을 참조할 수 있습니다.
3. data 모듈은 core 모듈과 presentation 모듈만을 참조할 수 있습니다.
  예시 3_1) data.book 모듈은 app 모듈을 참조할 수 없습니다.
  예시 3_2) data.book 모듈은 domain.book 모듈을 참조할 수 있습니다.
4. app 모듈 내 presentation 패키지는 domain, core 모듈만을 참조할 수 있습니다.
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
domain은 appinfo, book, login, oneline, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- useCase : presentation의 viewModel 클래스에서 사용할 useCase 정의
- repository : data 레이어에서 사용하는 repository의 인터페이스 선언
- model : 앱에서 사용하는 데이터 클래스

#### data
domain은 appinfo, book, google_auth, oneline, token, user 모듈로 세부 분리되어 있습니다.
각 모듈 내의 폴더구조는 아래와 같습니다.
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- model : data레이어에사만 사용되는 데이터 클래스 (dto)

#### 추후 계획
현재 presentation 패키지에 존재하는 각 화면들을 전부 fragment로 전환하고 navigation의 deeplink를 통해 화면 전환하도록 수정하여 SingleActivityApplication으로 변환한 다음, presentation 모듈로 분리할 예정입니다.
