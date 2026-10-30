# 아키텍처·검증 체계

점검일: 2026-10-30. 구조 변경 선호나 단순 스타일 경고는 이슈로 등록하지 않았다.

| ID | 우선순위 | 이슈 이름 | 위치 | 해결 방향 | 상태 |
| --- | --- | --- | --- | --- | --- |
| ARCH-001 | P1 | CI 매트릭스가 존재하지 않는 JVM·Linux 테스트 작업을 실행함 | `.github/workflows/gradle.yml:27` | 프로젝트에 `jvm`·`linuxX64` target이 없는데 `jvmTest`와 `linuxX64Test`를 실행한다. 각각 `--dry-run`으로 작업 없음 실패를 재현했다. 실제 지원 target의 작업으로 매트릭스를 정리하고 Android·iOS·지원되는 Wasm의 검증 범위를 명시한다. 실제 Gradle 작업 목록과 일치하는지 확인한다. | 해결 완료 |
| ARCH-003 | P2 | 레지스트리 테스트가 웹 대응 대상이 아닌 설정 항목까지 필수로 검사함 | `cmp-ui-docs/scripts/registry.test.mjs:127`, `cmp-ui-docs/web/catalog.js:40` | 모든 CMP catalog 항목에 웹 `meta.cmpIds` 대응을 강제해 `prefs-picker`에서 테스트가 실패한다. `cmp-pref` 설정 항목과 설치형 웹 UI의 대응 정책을 명시적으로 구분하고, 실제 웹 구현 대상에 대한 누락 검증을 유지한다. 테스트를 통과시키기 위해 무관한 picker의 대응 ID만 추가하지 않는다. | 해결 완료 |
| ARCH-002 | P3 | 예제의 commonTest가 Android 호스트 테스트에서 제외됨 | `example/composeApp/build.gradle.kts:20`, 같은 파일 `:85` | `commonTest`에 테스트가 있으나 Android target의 host test builder가 없다. 구성 단계 경고가 발생하고 `testAndroidHostTest`는 예제 테스트를 실행하지 않는다. 기존 라이브러리 모듈 방식에 맞춰 예제 Android 호스트 테스트를 활성화하고 `ScrollingBubbleScreenTest` 실행을 확인한다. iOS에서는 두 예제 테스트가 실행됐다. | 해결 완료 |
