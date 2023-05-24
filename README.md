# 책갈피:오늘한줄 - AOS

## 프로젝트 구조
본 프로젝트는 크게 4가지 폴더로 구성되어 있으며, 각 폴더의 대표 기능은 아래와 같습니다.
- app : application클래스 및 retrofit, okhttp, shared preference의 초기화작업, 의존성 주입
- presentation : 화면 및 화면에 사용되는 UI적인 요소
- domain : 앱에서 사용하는 데이터 클래스 및 비스니스 로직
- data : api호출과 같은 네트워크 작업 및 로컬 데이터베이스에 접근하여 데이터를 가져오는 작업을 수행

## 폴더 상세 구조
#### app
- di : 의존성 주입 모듈
- retrofit : retrofit초기 작업과 관련된 interceptor, http3 client, retrofit 객체
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
- useCase : presentation의 viewModel 클래스에서 사용할 useCase 정의
- repository : data 레이어에서 사용하는 repository의 인터페이스 선언
- models : 앱에서 사용하는 데이터 클래스

#### data
- datasource : repository가 데이터를 가져오는 대상인 데이터 저장소를 구현한 클래스
- repository_impl : domain 레이어에서 정의한 repository 인퍼테이스를 구현한 클래스
- api : retrofit2 인터페이스
- models : data레이어에사만 사용되는 데이터 클래스들 (ex. 서버의 response 데이터)
