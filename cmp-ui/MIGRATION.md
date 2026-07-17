# TDS Mobile to cmp-ui Migration Status

이 문서는 Toss Design System (TDS) Mobile 명세를 기준으로 `cmp-ui` 모듈에 마이그레이션된 컴포넌트 목록과 구현 상태를 추적합니다.

## 마이그레이션 현황 요약
- **총 대상 페이지/컴포넌트**: 52개
- **완료된 마이그레이션**: 45개
- **미완료/진행 예정**: 2개 (`Grid List`, `Chart`)
- **해당 없음 (가이드 문서)**: 5개 (`소개`, `시작하기`, `파운데이션`, `Asset 이해하기/활용하기`, `래핑한 컴포넌트 활용하기`)

---

## 컴포넌트 마이그레이션 현황표

| 번호 | TDS Mobile 컴포넌트/페이지 | 완료 여부 | 매핑된 cmp-ui 컴포넌트 | 소스 코드 파일 위치 | 비고 |
| :---: | :--- | :---: | :--- | :--- | :--- |
| 1 | 소개 | - | 해당 없음 | - | 가이드 문서 |
| 2 | 시작하기 | - | 해당 없음 | - | 가이드 문서 |
| 3 | 파운데이션 | - | 해당 없음 | - | 가이드 문서 |
| 4 | Badge | `[x]` | `IenBadge` | [IenBadge.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenBadge.kt) | 뱃지 컴포넌트 |
| 5 | Board Row | `[x]` | `IenBoardRow` | [IenBoardRow.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenBoardRow.kt) | 대시보드 리스트 피드 |
| 6 | Border | `[x]` | `IenBorder` | [IenLayout.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenLayout.kt) | 경계선 레이아웃 |
| 7 | Bottom Info | `[x]` | `IenBottomInfo` | [IenScreenParts.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenScreenParts.kt) | 푸터 정보 영역 |
| 8 | Bottom Sheet | `[x]` | `IenBottomSheet` | [IenDialogs.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenDialogs.kt) | 하단 슬라이드 시트 |
| 9 | Bubble | `[x]` | `IenBubble` | [IenBubble.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenBubble.kt) | 말풍선 컴포넌트 |
| 10 | Button | `[x]` | `IenButton`, `IenTextButton` | [IenButton.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenButton.kt#L66) | 댐핑 스프링 버튼 |
| 11 | Checkbox | `[x]` | `IenCheckbox` | [IenControls.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenControls.kt) | 체크박스 |
| 12 | Grid List | `[ ]` | (미구현) | - | 마이그레이션 예정 |
| 13 | Highlight | `[x]` | `IenHighlightText` | [IenContent.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenContent.kt#L82) | 텍스트 하이라이트 |
| 14 | Icon Button | `[x]` | `IenIconButton` | [IenButton.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenButton.kt#L116) | 원형 아이콘 버튼 |
| 15 | List Footer | `[x]` | `IenListFooter` | [IenList.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenList.kt#L159) | 리스트 광원 푸터 |
| 16 | List Header | `[x]` | `IenListHeader` | [IenList.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenList.kt#L64) | 리스트 서브 타이틀 헤더 |
| 17 | Loader | `[x]` | `IenLoader` | [IenPrimitives.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/primitives/IenPrimitives.kt#L115) | 스피너 로더 |
| 18 | Menu | `[x]` | `IenMenu` | [IenMenu.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenMenu.kt) | 오프셋 팝업 및 체크박스메뉴 |
| 19 | Modal | `[x]` | `IenModal` | [IenMenuModal.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenMenuModal.kt#L100) | 오버레이 모달 대화상자 |
| 20 | Numeric Spinner | `[x]` | `IenNumericSpinner` | [IenInputExtras.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenInputExtras.kt#L81) | 스피너 연동 수량 조절기 |
| 21 | Paragraph | `[x]` | `IenParagraph` | [IenTextContent.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenTextContent.kt#L17) | 본문 단락 배치 |
| 22 | Post | `[x]` | `IenPost` | [IenTextContent.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenTextContent.kt#L46) | 포스트 상세 뷰 |
| 23 | Progress Bar | `[x]` | `IenProgressBar` | [IenContent.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenContent.kt) | 진행 표시바 |
| 24 | Progress Stepper | `[x]` | `IenProgressStepper` | [IenContent.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenContent.kt) | 절차 스텝 표시기 |
| 25 | Rating | `[x]` | `IenRating` | [IenInputExtras.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenInputExtras.kt#L428) | 스타 평가 등급기 |
| 26 | Result | `[x]` | `IenResult` | [IenFeedback.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenFeedback.kt#L1277) | 최종 결과 피드백 화면 |
| 27 | Search Field | `[x]` | `IenSearchField` | [IenTextField.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenTextField.kt) | 실시간 초기화 검색창 |
| 28 | Segmented Control | `[x]` | `IenSegmentedControl` | [IenSelection.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenSelection.kt) | 탭/슬라이딩 세그먼트 컨트롤 |
| 29 | Skeleton | `[x]` | `IenSkeleton` | [IenFeedback.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenFeedback.kt#L689) | 스켈레톤 로딩 가이드 |
| 30 | Slider | `[x]` | `IenSlider` | [IenControls.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenControls.kt) | 슬라이더 조절바 |
| 31 | Stepper | `[x]` | `IenStepper` | [IenInputExtras.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenInputExtras.kt) | 단계 설정 스텝바 |
| 32 | Switch | `[x]` | `IenSwitch` | [IenControls.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenControls.kt) | 썸 스와이프 토글 스위치 |
| 33 | Tab | `[x]` | `IenTab` | [IenControls.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenControls.kt#L455) | 다중 스크린 탭 메뉴바 |
| 34 | Table Row | `[x]` | `IenTableRow` | [IenRows.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenRows.kt#L525) | 수평 데이터 요약 테이블 셀 |
| 35 | Text Button | `[x]` | `IenTextButton` | [IenButton.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenButton.kt#L172) | 경계선 없는 플랫 텍스트 버튼 |
| 36 | Snackbar | `[x]` | `IenSnackbarHost` / `showIenSnackbar` | [IenFeedback.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/feedback/IenFeedback.kt) | 알림 메시지 호스트 |
| 37 | Tooltip | `[x]` | `IenTooltip` | [IenFeedback.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenFeedback.kt) | 뾰족한 도움말 앵커 |
| 38 | Top | `[x]` | `IenTopBar` | [IenScreenParts.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenScreenParts.kt) | 타이틀 네비게이션 헤더바 |
| 39 | Agreement | `[x]` | `IenAgreement` | [IenRows.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenRows.kt) | 약관 전체 동의 바디 |
| 40 | Asset | `[x]` | `IenAssetFrame` | [IenAsset.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenAsset.kt) | 그래픽/벡터 프레이밍 에셋 |
| 41 | Asset 이해하기 | - | 해당 없음 | - | 가이드 문서 |
| 42 | Asset 활용하기 | - | 해당 없음 | - | 가이드 문서 |
| 43 | 래핑한 컴포넌트 활용하기 | - | 해당 없음 | - | 가이드 문서 |
| 44 | BottomCTA | `[x]` | `IenBottomCTA` | [IenScreenParts.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenScreenParts.kt) | 하단 밀착 고정 액션바 |
| 45 | Chart | `[ ]` | (미구현) | - | 마이그레이션 예정 |
| 46 | Dialog | `[x]` | `IenAlertDialog` | [IenDialogs.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenDialogs.kt) | 모달 메시지 대화상자 |
| 47 | Keypad | `[x]` | `IenKeypad` | [IenKeypads.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenKeypads.kt) | 셔플 난독화 숫자 키패드 |
| 48 | ListRow | `[x]` | `IenListRow` | [IenRows.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/composite/IenRows.kt) | 한 줄 구성 옵션 셀 |
| 49 | TextField | `[x]` | `IenTextField` | [IenTextField.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenTextField.kt) | 텍스트 필드 |
| 50 | SplitTextField | `[x]` | `IenSplitTextField` | [IenTextField.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenTextField.kt#L523) | 분할 숫자/코드 문자 입력창 |
| 51 | TextArea | `[x]` | `IenTextArea` | [IenTextField.kt](file:///Users/ienground/IEN_DATA/Developments/AndroidLibrary/ienlab-cmp-library/cmp-ui/src/commonMain/kotlin/zone/ien/utils/ui/components/interactive/IenTextField.kt#L254) | 여러 줄 입력 텍스트 영역 |
