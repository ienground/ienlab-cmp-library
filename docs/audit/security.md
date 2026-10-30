# 보안

점검일: 2026-10-30. 로그·화면 보호는 소스에서 계약 위반을 확인했다. 실제 개인정보 유출이나 운영 서비스 침해가 발생했다는 의미는 아니다.

| ID | 우선순위 | 이슈 이름 | 위치 | 해결 방향 | 상태 |
| --- | --- | --- | --- | --- | --- |
| SEC-001 | P2 | iOS 로그가 `init(false)` 설정을 무시함 | `cmp-utils/src/iosMain/kotlin/zone/ien/utils/utils/Dlog.ios.kt:33` | `e`·`w`·`i`·`d`·`v`가 모두 무조건 `NSLog`를 호출한다. Android actual처럼 모든 레벨에서 `isDebug`를 확인한다. `init(false)`와 초기 기본 상태에서는 메시지를 출력하지 않는지 검증한다. 민감한 문자열을 전달한 소비 앱에서는 릴리스 로그 노출로 이어질 수 있다. | 해결 완료 |
| SEC-002 | P2 | 여러 Android Window에서 화면 캡처 보호가 누락됨 | `cmp-utils/src/androidMain/kotlin/zone/ien/utils/utils/SecureWindowManager.kt:8`, `cmp-utils/src/androidMain/kotlin/zone/ien/utils/utils/ProtectScreenshotWrapper.android.kt:13` | 전역 참조 수가 1일 때만 플래그를 설정해 A창 acquire 뒤 B창 acquire 시 B창은 보호되지 않는다. 해제 순서에 따라 A창 플래그도 남는다. Window별 참조 수로 관리하고 최초 플래그 보유 여부를 보존한다. 두 Activity/Window가 겹치는 acquire·release 순서와 동일 창 중첩을 검증한다. | 해결 완료 |
| SEC-003 | P2 | 네트워크에 공개하는 개발 서버가 취약 Vite 버전에 고정됨 | `cmp-ui-docs/package.json:48` | Vite를 8.3.5로 갱신해 해당 보안 패치를 반영했다. TypeScript 검사·프로덕션 빌드·레지스트리 빌드와 Chrome 입력 검증을 통과했다. 최초 영향 버전과 공격 조건은 아래 권고 링크를 참고한다. | 해결 완료 |
| SEC-004 | P3 | shadcn 전이 deepmerge 취약점의 패치 버전 미공개 | `cmp-ui-docs/package.json:9`, `cmp-ui-docs/scripts/registry.mjs:94` | 기존 registry 수집·검증 스크립트에 정적 JSON 출력을 추가하고 shadcn 빌드 CLI 의존성을 제거해 deepmerge 전이 경로를 없앴다. 쓰기 전에 항목 이름·경로 탈출·registry.json 충돌을 검사한다. 필요한 공식 Tailwind 스타일은 MIT 고지와 함께 로컬에 보존했다. 공식 CLI와 131개 JSON이 일치하고 빌드 CSS도 바이트 단위로 동일하며 npm audit은 전체 0개다. | 해결 완료 |

## 의존성 검증 근거

- 최초 `npm audit --json`: 12개(높음 8·보통 3·낮음 1). Vite·shadcn 갱신 후 낮음 경고 3개 노드까지 줄였고, 후속 CLI 제거로 현재 전체 0개다. 동일 전이 권고를 여러 이슈로 합산하지 않았다.
- `npm audit --omit=dev --json`: 0개. 이것만으로 런타임 의존성의 모든 취약점이 없다고 보장하지 않는다.
- Vite 패치 버전과 조건: [파일 접근 제한 우회 권고](https://github.com/advisories/GHSA-rq7h-c2jc-7f22), [Wasm 오류 메시지 노출 권고](https://github.com/advisories/GHSA-vfpm-58rq-9qcg).
- 전이 의존성의 조건부 위험: [MCP SDK 세션 격리 권고](https://github.com/advisories/GHSA-22jm-h49p-29qw), [Zod 메모리 소진 권고](https://github.com/advisories/GHSA-mrhx-c7xc-m3w5). 최초 악용 가능성 검토 근거이며, 현재 해당 CLI 전이 의존성은 제거했다.

후속 수정 검증: `init(false)` 로그 차단은 iOS actual 소스를 Kotlin/Native macOS 실행 파일로 컴파일해 확인했고, Window별 보호와 기존 FLAG_SECURE 보존은 Android 17 에뮬레이터에서 확인했다.

최초 미해결 사유: [deepmerge 공식 권고](https://github.com/advisories/GHSA-gcqx-w48m-w2h4)는 2026-10-30 확인 시 수정 버전을 제공하지 않았다. 패치를 기다리는 대신 프로젝트에서 해당 전이 의존성을 제거해 해결했다.

빌드 도구 대체 근거: [shadcn CLI 문서](https://ui.shadcn.com/docs/cli)의 정적 registry 산출물을 기준으로 기존 수집기를 확장했다. 공식 CSS 원본과 라이선스는 `cmp-ui-docs/web/shadcn-tailwind.css`에 보존한다.
