package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/*
 * 플랫폼 내비게이션 바(예: iOS UIKit UINavigationController가 띄운 화면의 바)는 compose-hig의
 * `LocalCupertinoNativeBarsPadding`이 알려 준다. IEN 컴포넌트도 같은 값을 따른다.
 * 앱은 값 하나만 제공하면 Cupertino와 IEN 두 디자인이 함께 플랫폼 바에 맞춰진다.
 */

/** [padding]을 레이아웃 시점에 읽는 [WindowInsets]입니다. 같은 [padding]이면 같은 값입니다. */
@Stable
internal class NativeBarsWindowInsets(private val padding: PaddingValues) : WindowInsets {
    override fun getTop(density: Density): Int = with(density) { padding.calculateTopPadding().roundToPx() }

    override fun getBottom(density: Density): Int = with(density) { padding.calculateBottomPadding().roundToPx() }

    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int =
        with(density) { padding.calculateLeftPadding(layoutDirection).roundToPx() }

    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int =
        with(density) { padding.calculateRightPadding(layoutDirection).roundToPx() }

    override fun equals(other: Any?): Boolean = other is NativeBarsWindowInsets && other.padding === padding

    override fun hashCode(): Int = padding.hashCode()
}

/**
 * 플랫폼 바가 있을 때 [IenScaffold] 콘텐츠가 받는 여백입니다. 한 객체가 레이아웃 시점에 [native]를 읽어,
 * 큰 제목이 접히는 동안처럼 여백이 프레임마다 바뀌어도 콘텐츠를 다시 그리지 않습니다.
 *
 * @param bottomBarHeight 스캐폴드 하단 바의 높이. 없으면 `null`이고 아래 여백은 [native]를 따릅니다.
 */
@Stable
internal class NativeBarsContentPadding(
    private val native: PaddingValues,
    private val bottomBarHeight: () -> Dp?,
) : PaddingValues {
    override fun calculateTopPadding(): Dp = native.calculateTopPadding()

    override fun calculateBottomPadding(): Dp = bottomBarHeight() ?: native.calculateBottomPadding()

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp = native.calculateLeftPadding(layoutDirection)

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp = native.calculateRightPadding(layoutDirection)
}
