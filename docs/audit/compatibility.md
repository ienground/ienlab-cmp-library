# 호환성·Deprecated API

점검일: 2026-10-30. Kotlin 2.4.21, Compose Foundation 1.12.2, Material3 1.12.0-alpha03, kotlinx-datetime 0.8.0 환경의 실제 컴파일 경고와 공식 문서로 확인했다. 현재 컴파일은 통과한다. 상태는 후속 수정 결과이며, COMPAT-004는 Android 기기 검증에서 추가로 발견했다.

| ID | 우선순위 | 이슈 이름 | 위치 | 해결 방향 | 상태 |
| --- | --- | --- | --- | --- | --- |
| COMPAT-004 | P2 | Android 숫자 포맷이 Java 기준 반올림·그룹 간격과 다름 | `cmp-utils/src/androidMain/kotlin/zone/ien/utils/utils/DecimalFormat.android.kt:7` | Android ICU의 보조 그룹 간격을 Java 기준의 마지막 그룹 간격으로 정규화했다. 십진 반올림 경계에서만 정확한 Double의 BigDecimal 값을 사용해 이진 오차를 보존하고 일반 입력의 기존 표시를 유지한다. 기존 JVM 호스트·Android 17 기기 포맷 테스트가 모두 통과했다. 최초 실패 예시는 2.675 → 2.68, #,##,##0의 12345678 → 1,23,45,678이었다. | 해결 완료 |
| COMPAT-001 | P3 | SwipeBox가 폐기된 AnchoredDraggableState 생성자를 사용함 | `cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/list/IenSwipeBox.kt:835`, 같은 파일 `:930` | 폐기된 생성자를 제거하고 전용 IenSwipeBoxState가 거리·속도 임계값과 스프링·decay를 소유하도록 이행했다. DraggableState를 SDK anchoredDrag에 연결하고 공개 animateToWithDecay로 정착시켜 기존 설정을 보존한다. SDK 상태 직접 전달 오버로드는 유지했다. 팩터리 반환형·공유 열린 상태 타입 변경과 바이너리 재컴파일은 아래 이행 안내를 따른다. 기존 설정 비교·RTL·전체 스와이프 실행/복원·key 재생성 UI 테스트를 통과했다. | 해결 완료 |
| COMPAT-002 | P3 | 보안 입력 필드의 기본 라벨 위치가 deprecated 타입임 | `cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/view/textfield/BasicTextField.kt:247` | 기본값 `TextFieldLabelPosition.Attached()`를 실제 filled/outlined 장식에 맞는 `Inside` 또는 `Cutout`으로 바꾼다. 공개 `labelPosition` 인자는 유지하고 라벨 확대·축소 및 테두리 겹침을 확인한다. | 해결 완료 |
| COMPAT-003 | P3 | 플레이그라운드 날짜 표시가 deprecated monthNumber를 사용함 | `example/composeApp/src/commonMain/kotlin/zone/ien/utils/example/ui/screens/playground/IenPlaygroundScreen.kt:107` | `date.monthNumber`를 `date.month.number`로 바꾸고 기존 `YYYY-MM-DD`의 0 패딩을 보존한다. enum 자체를 문자열로 출력하지 않도록 한다. | 해결 완료 |

## 검증 근거

- `compileKotlinIosSimulatorArm64 :cmp-ui-docs:compileKotlinWasmJs --warning-mode all`에서 세 경고를 확인했다.
- Foundation 1.12.2와 kotlinx-datetime 0.8.0의 실제 source JAR에서도 폐기 안내와 대체 표현을 확인했다.
- [AnchoredDraggableState 공식 API](https://developer.android.com/reference/kotlin/androidx/compose/foundation/gestures/AnchoredDraggableState)는 임계값과 애니메이션을 fling behavior로 옮기도록 안내한다.
- [TextFieldLabelPosition 공식 API](https://developer.android.com/reference/kotlin/androidx/compose/material3/TextFieldLabelPosition)는 `Attached`의 대체 타입을 명시한다.
- [kotlinx-datetime 공식 변경 이력](https://github.com/Kotlin/kotlinx-datetime/blob/master/CHANGELOG.md)과 [LocalDate 공식 API](https://kotlinlang.org/api/kotlinx-datetime/kotlinx-datetime/kotlinx.datetime/-local-date/)에서 월 속성 변경을 확인했다.

## SwipeBox API 이행

`rememberIenSwipeBoxState`의 반환형이 `AnchoredDraggableState<IenSwipeBoxStates>`에서 `IenSwipeBoxState`로 바뀌었다. 기존 타입에는 플링 설정을 저장할 수 없어 필요한 변경이며, 이전 바이너리 소비자는 재컴파일해야 한다.

- 타입 추론으로 상태를 만들고 `IenSwipeBox(state = state)`에 전달하는 기존 소스는 그대로 사용할 수 있다.
- 명시적 상태 타입은 `IenSwipeBoxState`로 변경한다. 공유 열린 상태는 `MutableState<IenSwipeBoxState?>`로 변경한다.
- 직접 Compose API에 접근할 때만 `state.anchoredDraggableState`를 사용한다. `IenSwipeBox`에는 전용 상태를 전달해야 사용자 플링 설정도 연결된다.
- 직접 만든 SDK 상태를 받는 기존 `IenSwipeBox` 오버로드도 유지한다. 이 경로는 전달된 SDK 상태의 동작을 사용한다.

```kotlin
val openRow = remember { mutableStateOf<IenSwipeBoxState?>(null) }
val state = rememberIenSwipeBoxState(openSwipeBoxState = openRow)
IenSwipeBox(state = state, actionItemBuilder = { start { Text("액션") } }) {
    Text("항목")
}
// SDK API 연동이 필요한 경우에만 사용합니다.
val anchors: AnchoredDraggableState<IenSwipeBoxStates> = state.anchoredDraggableState
```
