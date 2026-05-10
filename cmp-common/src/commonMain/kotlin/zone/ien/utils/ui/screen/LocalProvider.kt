package zone.ien.utils.ui.screen

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 스크롤 틴트가 활성화되는지를 제어하는 CompositionLocal
 *
 * 이 CompositionLocal은 스크롤 가능한 컴포넌트에 스크롤 틴트 효과를 적용할지 여부를 결정합니다.
 * 기본 값은 true로, 스크롤 틴트가 활성화되어 있습니다.
 */
val LocalIsScrollTint: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }

/**
 * M3 상단 바 크기를 정의하는 CompositionLocal
 *
 * 이 CompositionLocal은 Material 3 상단 바의 크기를 제공합니다.
 * 기본 값은 TopBarSize.Small으로 설정되어 있습니다.
 */
val LocalM3TopBarSize: ProvidableCompositionLocal<TopBarSize> = staticCompositionLocalOf { TopBarSize.Small }

/**
 * 네비게이션 제목 표시 여부를 제어하는 CompositionLocal
 *
 * 이 CompositionLocal은 UI에서 네비게이션 제목을 표시할지 여부를 결정합니다.
 * 기본 값은 false로 설정되어 있습니다.
 */
val LocalHigShowNavTitle: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }

/**
 * M3 상단 바의 중앙 정렬 여부를 제어하는 CompositionLocal
 *
 * 이 CompositionLocal은 M3 상단 바의 내용이 중앙 정렬되는지를 결정합니다.
 * 기본 값은 false로 설정되어 있습니다.
 */
val LocalIsM3TopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }

/**
 * HIG 상단 바의 중앙 정렬 여부를 제어하는 CompositionLocal
 *
 * 이 CompositionLocal은 HIG (Human Interface Guidelines) 상단 바의 내용이 중앙 정렬되는지를 결정합니다.
 * 기본 값은 true로 설정되어 있습니다.
 */
val LocalIsHigTopBarCenterAligned: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }

/**
 * IME 패딩 활성화 여부를 제어하는 CompositionLocal
 *
 * 이 CompositionLocal은 IME (입력 방법 편집기) 패딩이 UI에 적용되는지를 제어합니다.
 * 기본 값은 true로 설정되어 있습니다.
 */
val LocalEnableImePadding: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }

/**
 * IME 패딩 설정을 제공하는 CompositionLocal
 *
 * 이 CompositionLocal은 UI의 IME 패딩을 설정하는 함수를 제공합니다.
 * 기본 값은 빈 람다입니다.
 */
val LocalSetEnableImePadding: ProvidableCompositionLocal<(Boolean) -> Unit> = staticCompositionLocalOf { {} }