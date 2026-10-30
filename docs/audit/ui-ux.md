# UI/UX

점검일: 2026-10-30. 아래 항목은 소스·호출 경로 또는 lint로 확인했다. 실제 기기의 그래픽 캡처 검증은 하지 않았다.

| ID | 우선순위 | 이슈 이름 | 위치 | 해결 방향 | 상태 |
| --- | --- | --- | --- | --- | --- |
| UI-001 | P2 | 세 종류 체크박스가 선택·비활성 상태를 접근성에 알리지 않음 | `cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/interactive/IenSelection.kt:801`, 같은 파일 `:946`, `:1072` | 원형·점·라인 체크박스는 `clickable(enabled = true, role = Role.Checkbox)`만 제공한다. 스크린리더가 현재 체크 상태와 비활성 상태를 판단할 수 있도록 `toggleableState` 등 상태 semantics와 disabled semantics를 제공한다. 비활성 클릭 시 흔들림 동작은 보존하고 TalkBack·VoiceOver에서 라벨·선택·비활성 낭독을 확인한다. | 해결 완료 |
| UI-002 | P2 | 적응형 체크박스의 Cupertino 분기에서 라벨이 사라짐 | `cmp-adaptive/src/commonMain/kotlin/zone/ien/utils/adaptive/component/Checkbox.kt:39`, 같은 파일 `:74`, `:109` | 공개 `label`이 Material 분기만 렌더링되고 Cupertino 분기는 받지 않는다. 세 변형에서 기존 적응형 레이아웃으로 라벨을 함께 제공하고 클릭 영역·비활성 상태·접근성 이름을 유지한다. `label`이 있는 샘플을 양 플랫폼에서 비교한다. | 해결 완료 |
| UI-003 | P2 | HIG 메뉴 아이콘 전환이 이전·다음 상태를 구분하지 않음 | `cmp-adaptive/src/commonMain/kotlin/zone/ien/utils/adaptive/menu/MenuItems.kt:84` | `AnimatedContent`의 target 인자 대신 바깥 최신 `icon`을 사용해 퇴장 콘텐츠까지 새 아이콘으로 바뀐다. content 람다의 target 아이콘을 렌더링하고 null 처리도 해당 상태를 기준으로 한다. 아이콘 변경 전환을 확인한다. Android lint의 `UnusedContentLambdaTargetStateParameter` 오류도 해소해야 한다. | 해결 완료 |
