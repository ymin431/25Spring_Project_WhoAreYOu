## 🌿 브랜치 전략

| 브랜치 이름         | 설명                                                                 | 비고 |
|--------------------|----------------------------------------------------------------------|------|
| `main`             | 배포를 위한 메인 브랜치                                                |      |
| `feature/기능명`   | 새 기능 개발 브랜치. 기능명은 소문자로 작성. 구분 필요 시(`-`) 사용 | 예: `feature/login`, `feature/userprofile`, `feature/login-oauth` |

---

## ✨ 커밋 컨벤션

| 타입        | 설명                             | 예시 메시지                |
|-------------|----------------------------------|----------------------------|
| `feat`      | 새로운 기능 추가                   | `feat: 로그인 기능 추가`    |
| `fix`       | 버그 수정                         | `fix: 결제 오류 수정`      |
| `refactor`  | 기능 변화 없이 코드 개선/리팩터링   | `refactor: 인증 로직 개선` |
| `chore`     | 문서 작성, 포맷팅, 환경 설정 등 기타 작업 | `chore: 라이브러리 버전 업데이트` |

---

## 🧬 디렉토리 구조
```
com.example.whoareyou/
│
├── component/                     # 공통 UI 컴포넌트
│   ├── BottomBar.kt              # 하단 네비게이션 바 정의 (BottomTab enum 포함)
│   ├── TopBar.kt                 # 상단 탭/타이틀 등 정의
│   └── CustomModifier.kt         # 공통 Modifier 확장 함수 (e.g. noRippleClickable 등)
│
├── home/                         # 홈 화면 관련 코드
│   └── HomeScreen.kt            # 홈 화면 UI 및 최근 연락처, 촬영 버튼 등 구성
│
├── login/                        # 로그인 관련 로직
│   ├── LoginScreen.kt           # 로그인 UI 구성 (Google 로그인 버튼 포함)
│   └── LoginViewModel.kt        # 로그인 상태 관리 및 Google Sign-In 로직 처리
││
├── MainActivity.kt               # 앱 진입점, 로그인 여부에 따라 화면 분기
└── MainScreen.kt                 # 로그인 이후의 메인 화면 (Scaffold + BottomBar 포함)
```