package zone.ien.utils.icon

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * UI에서 사용할 아이콘 종류와 형태입니다.
 */
sealed interface IconStyle {
    /** Tabler 아이콘을 사용합니다. */
    data object Tabler : IconStyle

    /** Material 아이콘의 형태입니다. */
    enum class Material : IconStyle {
        /** 채워진 아이콘입니다. */
        Filled,

        /** 모서리가 둥근 아이콘입니다. */
        Rounded,

        /** 모서리가 뾰족한 아이콘입니다. */
        Sharp
    }
}

/**
 * 하위 컴포저블에서 사용할 아이콘 스타일을 제공합니다.
 * 기본값은 [IconStyle.Material.Filled]입니다.
 *
 * 아이콘을 사용하는 컴포저블에서 현재 값을 읽어 사용할 아이콘을 선택할 수 있습니다.
 */
val LocalIconStyle: ProvidableCompositionLocal<IconStyle> =
    staticCompositionLocalOf { IconStyle.Material.Filled }

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