# 프로젝트 품질 점검 및 수정 현황

마지막 점검·수정 날짜: **2026-10-30 (Asia/Seoul)**

최초 감사는 소스 변경 없이 24개 이슈를 기록했다. 후속 수정 요청에 따라 중요도순으로 수정했고, Android 기기 검증에서 확인한 숫자 포맷 호환성 문제 `COMPAT-004`를 추가했다. 현재 **25개 모두 해결**이다. 기존 `docs/`의 pull-to-refresh 문서와 사용자 미추적 파일은 보존했다.

표의 원인·위치는 최초 발견 시점을 기준으로 할 수 있으며, 상태는 후속 수정 결과다. 동일 원인의 플랫폼·SDK별 중복은 하나로 통합했다.

## 이슈 현황

| 카테고리 | 전체 | 해결 완료 | 미해결 | 문서 |
| --- | ---: | ---: | ---: | --- |
| 버그 | 10 | 10 | 0 | [bugs.md](bugs.md) |
| 보안 | 4 | 4 | 0 | [security.md](security.md) |
| UI/UX | 3 | 3 | 0 | [ui-ux.md](ui-ux.md) |
| 성능 | 1 | 1 | 0 | [performance.md](performance.md) |
| 호환성·deprecated API | 4 | 4 | 0 | [compatibility.md](compatibility.md) |
| 아키텍처·검증 체계 | 3 | 3 | 0 | [architecture.md](architecture.md) |
| **합계** | **25** | **25** | **0** | |

| 우선순위 | 전체 | 해결 완료 | 미해결 |
| --- | ---: | ---: | ---: |
| P0 | 0 | 0 | 0 |
| P1 | 2 | 2 | 0 |
| P2 | 18 | 18 | 0 |
| P3 | 5 | 5 | 0 |

상태: **해결 완료 25개, 미해결 0개, 검증 필요 0개**. npm의 취약 패키지 노드 수는 독립 이슈 수와 다르다.

## 남은 이슈

등록된 이슈 중 미해결·검증 필요 항목은 없다. SwipeBox의 반환 타입과 공유 상태 타입이 변경되므로 [API 이행 안내](compatibility.md)를 적용하고 소비자를 재컴파일해야 한다. 실제 기기 전체 화면·운영 서비스·원격 배포의 검증 한계는 아래에 별도로 기록했다.

## 주요 수정 결과

| 영역 | 적용 내용 |
| --- | --- |
| 데이터·파일 | 두 Firebase SDK의 시간 정밀도·음수 epoch·Legacy 삭제 payload, 윤일 연산, Android FileWrapper 변환·업로드 실패 전달, 압축 목표 실패·취소 전파, Apple 빈 데이터 변환 수정 |
| 보안 | iOS 비활성 로그 차단, Window별 참조 수·기존 FLAG_SECURE 보존, Vite 8.3.5 업데이트 및 취약 전이 의존성을 끌어오던 shadcn 빌드 CLI 제거 |
| UI·웹 입력 | 체크박스 선택·비활성 semantics와 Cupertino 라벨·라벨 클릭, 메뉴 아이콘 전환 상태, 다이얼로그 파생 상태 보존, OTP 전체 입력·붙여넣기·포커스 및 소수 step 보정 |
| 호환성 | SwipeBox 전용 상태와 공개 앵커 API로 이행, 보안 입력 라벨 위치·월 번호 deprecated 사용 교체, Android ICU와 JVM의 숫자 반올림·그룹 간격 차이 보정 |
| 검증 체계 | CI를 실제 Android·iOS·Wasm 작업에 연결, 예제·filekit-core Android host 테스트 활성화, 웹 대응 여부를 명시한 카탈로그 검증, 회귀 테스트 추가 |

새 내부 로직은 외부 SDK 경계의 압축·삭제 payload 정책과 웹 입력 정규화를 검증하기 위해 분리했다. 웹 로직 파일은 실제 registry 설치 대상에도 포함했다. Compose UI 테스트 의존성은 현재 Compose 버전을 재사용해 iOS 테스트에만 추가했다. SwipeBox는 팩터리 반환형과 공유 열린 상태 타입이 변경되어 소비자 재컴파일이 필요하다. 직접 SDK 상태를 전달하는 컴포넌트 오버로드는 유지했다. [API 이행 안내](compatibility.md)를 참고한다. shadcn 빌드 도구를 제거하면서 실제 사용 중이던 Tailwind 스타일은 MIT 고지와 함께 로컬 파일로 보존했다.

## 실행한 검증

JDK 21.0.6, Kotlin 2.4.21, Node 26.12.0, npm 11.20.0 환경이다. 최초 감사에서는 웹 생성기를 임시 복사본에서 실행했고, 후속 수정에서는 원본 프로젝트에서 정상 설치·생성·빌드 절차를 실행했다. 무관한 생성 파일 변경은 남기지 않았다.

| 검증 | 결과 |
| --- | --- |
| `testAndroidHostTest` | **151개 통과**, 예제의 두 commonTest와 새 회귀 테스트 포함 |
| `iosSimulatorArm64Test` | **146개 통과**, 체크박스와 SwipeBox의 임계값·RTL·전체 스와이프·재생성 UI 회귀 포함 |
| `lintAndroidMain` | 오류 **0건**. 기존 스타일·실험 API 등의 경고는 별도이며 제거했다고 주장하지 않음 |
| `compileKotlinIosSimulatorArm64`, `:cmp-ui-docs:compileKotlinWasmJs` | 성공. COMPAT-001의 deprecated 생성자 사용 제거 확인 |
| `:cmp-utils:connectedAndroidDeviceTest`, 두 filekit 모듈과 cmp-date의 `connectedAndroidDeviceTest` | Android 17 Pixel_10_Pro 에뮬레이터에서 **17개 통과**. 창 보호·숫자 포맷·파일 변환·빈 데이터 포함 |
| `npm test`, `npm run test:preview` | **14개 + 132개 통과**. 최초 prefs-picker 카탈로그 검증 실패 해소 |
| `npm run build`, `npm run build:registry` | 생성·TypeScript 검사·Vite 프로덕션 빌드·helper 포함 registry 빌드 성공. 새 숫자 연산 로직은 ES2017 대상 타입 검사도 통과 |
| 빌드 도구 대체 동등성 | 새 registry 빌더의 JSON 131개가 공식 CLI 4.23.0 출력과 의미상 일치, 로컬에 보존한 공식 Tailwind 스타일의 빌드 CSS는 바이트 단위로 동일 |
| Chrome 실제 DOM | OTP 123456 전체 입력·789012 붙여넣기·끝칸 포커스, 숫자 스피너 0.2 → 0.3 → 0.4 확인. OS의 실제 SMS 수신 자동 완성은 미검증 |
| `npm audit --json`, `npm audit --omit=dev --json` | 개발 의존성을 포함한 전체 **0개**. 최초 12개 경고를 모두 해소 |
| iOS Dlog actual 소스의 Kotlin/Native macOS 실행 | 초기/재설정 비활성 로그 6개 차단과 활성 로그 출력 확인 |
| 회귀 검출 확인 | 수정 전 코드에서 1ms 시간 정밀도, 윤일 예외, FileWrapper URI 누락, 체크박스 상태·라벨 누락, OTP 잘림·소수 step 오차를 재현. Android 숫자 포맷의 기존 공통 테스트 실패 후 수정된 기기 테스트 통과 |

## 점검 범위와 남은 한계

| 영역 | 확인 내용·제한 |
| --- | --- |
| 모듈 범위 | cmp-common·date·utils·pref·navigation·coil, Firebase·FileKit 전체 변형, cmp-ui·adaptive·ui-kit·icon, example, cmp-ui-docs·웹 registry, Gradle·GitHub Actions·Firebase Hosting 설정을 점검 |
| UI·그래픽 | UIKit 대상 라벨·semantics, SwipeBox 제스처 UI 테스트와 웹 입력 DOM은 검증했다. 실제 기기별 전체 화면 캡처, 다크 모드·글자 확대·회전·클리핑·모든 애니메이션은 전수 검증하지 않음 |
| 접근성 | 상태·라벨·비활성 semantics와 라벨 터치를 확인했다. 실제 TalkBack·VoiceOver 음성 낭독과 모든 브라우저 키보드 동작은 미검증 |
| Firebase 서비스 | 운영 로그인·계정 연결·삭제·업로드는 실행하지 않았다. payload·변환·취소 진입 조건을 수정했으나 이미 시작된 SDK 요청의 취소까지 보장하지 않음. 서버 보안 규칙·Console 권한은 저장소에 없어 감사 불가 |
| 기기·OS | Android 17 에뮬레이터와 iOS simulator 테스트를 사용했다. Android 전체 OS 범위와 iOS 실기기 서명·설치·전체 앱 UI는 미검증 |
| Wasm·배포 | Wasm 컴파일은 확인했으나 브라우저별 Compose 런타임, 전체 배포 번들, 원격 CI·Maven 배포물·운영 Hosting은 미검증 |
| 성능·의존성 | 프레임·CPU·메모리·네트워크 프로파일링, Gradle/Maven 전체 CVE 대조, Git 이력의 비밀 정보 전수 검사는 미수행. npm 경고 0개만으로 모든 취약점 부재를 보장하지 않음 |
| 기존 사용자 작업 | cmp-ui-docs/web/generated-project-presets.json 및 cmp-ui-web-registry/projects/는 수정·스테이징하지 않음 |

소스·테스트·설정 및 이 감사 문서의 작업 변경만 스테이징하며, 커밋·푸시는 수행하지 않는다.
