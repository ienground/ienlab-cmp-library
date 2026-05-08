package zone.ien.utils.icon

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 아이콘 스타일을 나타내는 열거형 클래스
 *
 * 이 열거형은 아이콘 표시에 사용할 수 있는 아이콘 스타일(Filled, Rounded, Sharp)을 정의합니다.
 */
enum class IconStyle {
    /**
     * 채워진 아이콘 스타일
     * 완전히 채워진 아이콘을 나타냅니다.
     */
    Filled,

    /**
     * 라운드된 아이콘 스타일
     * 모서리가 둥근 아이콘을 나타냅니다.
     */
    Rounded,

    /**
     * 샤프 아이콘 스타일
     * 예각이 뾰족하고 선이 정확한 아이콘을 나타냅니다.
     */
    Sharp
}

/**
 * 현재 아이콘 스타일을 정의하는 CompositionLocal
 *
 * 이 CompositionLocal은 전체 UI에서 사용되는 아이콘 스타일(Filled, Rounded, Sharp)을 정의합니다.
 */
val LocalIconStyle: ProvidableCompositionLocal<IconStyle> = staticCompositionLocalOf { IconStyle.Filled }

/**
 * 뒤로 가기 버튼 아이콘을 정의하는 CompositionLocal
 *
 * 이 CompositionLocal은 UI에서 뒤로 가기 버튼에 사용할 사용자 정의 아이콘을 제공합니다.
 * 값이 null인 경우 기본 뒤로 가기 버튼이 사용됩니다.
 */
val LocalBackButtonIcon: ProvidableCompositionLocal<IconData?> = staticCompositionLocalOf { null }

/**
 * 닫기 버튼 아이콘을 정의하는 CompositionLocal
 *
 * 이 CompositionLocal은 UI에서 닫기 버튼에 사용할 사용자 정의 아이콘을 제공합니다.
 * 값이 null인 경우 기본 닫기 버튼이 사용됩니다.
 */
val LocalCloseButtonIcon: ProvidableCompositionLocal<IconData?> = staticCompositionLocalOf { null }