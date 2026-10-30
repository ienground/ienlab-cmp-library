# 성능

점검일: 2026-10-30. 프로파일링 수치 없이 프레임 저하나 메모리 누수를 단정하지 않았다.

| ID | 우선순위 | 이슈 이름 | 위치 | 해결 방향 | 상태 |
| --- | --- | --- | --- | --- | --- |
| PERF-001 | P2 | 입력 다이얼로그의 파생 상태를 재구성마다 재생성함 | `cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/dialog/TextFieldDialog.kt:144` | `derivedStateOf`를 `remember`하지 않아 해당 함수가 재구성될 때마다 상태 객체와 전체 필드 검증 계산을 새로 만든다. `textStates`와 검증 함수가 담긴 `textFields` 변경을 반영하도록 파생 상태를 기억하거나 단순 계산으로 바꾼다. 최신 validator 적용과 확인 버튼 갱신을 검증한다. Android lint가 `UnrememberedMutableState` 오류로 실패하므로 우선순위를 P2로 두었다. | 해결 완료 |
