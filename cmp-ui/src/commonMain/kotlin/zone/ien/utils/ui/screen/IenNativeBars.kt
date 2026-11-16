package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

/*
 * 플랫폼 내비게이션 바(예: iOS UIKit UINavigationController가 띄운 화면의 바)는 compose-hig의
 * `LocalCupertinoNativeBarsPadding`이 알려 준다. IEN 컴포넌트도 같은 값과 같은 규칙(`withCupertinoNativeBars`)을 따른다.
 * 앱은 값 하나만 제공하면 Cupertino와 IEN 두 디자인이 함께 플랫폼 바에 맞춰진다.
 */

/**
 * 플랫폼 바가 있을 때 [IenScaffold] 콘텐츠가 받는 여백입니다. 한 객체가 레이아웃 시점에 [insets]를 읽어,
 * 큰 제목이 접히는 동안처럼 여백이 프레임마다 바뀌어도 콘텐츠를 다시 그리지 않습니다.
 *
 * @param insets 플랫폼 바에 맞춘 콘텐츠 인셋(`contentWindowInsets.withCupertinoNativeBars()`)
 * @param bottomBarHeight 스캐폴드 하단 바의 높이. 없으면 `null`이고 아래 여백은 [insets]를 따릅니다.
 */
@Stable
internal class NativeBarsContentPadding(
    private val insets: WindowInsets,
    private val screenDensity: Density,
    private val bottomBarHeight: () -> Dp?,
) : PaddingValues {
    override fun calculateTopPadding(): Dp = insets.getTop(screenDensity).pxToDp()

    override fun calculateBottomPadding(): Dp = bottomBarHeight() ?: insets.getBottom(screenDensity).pxToDp()

    override fun calculateLeftPadding(layoutDirection: LayoutDirection): Dp =
        insets.getLeft(screenDensity, layoutDirection).pxToDp()

    override fun calculateRightPadding(layoutDirection: LayoutDirection): Dp =
        insets.getRight(screenDensity, layoutDirection).pxToDp()

    private fun Int.pxToDp(): Dp = with(screenDensity) { this@pxToDp.toDp() }
}
