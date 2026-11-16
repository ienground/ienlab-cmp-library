package zone.ien.utils.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import zone.ien.hig.LocalCupertinoNativeBarsPadding
import zone.ien.navigation.LocalNativeScreen
import zone.ien.navigation.NativeBarButton
import zone.ien.navigation.NativeBarItems
import zone.ien.navigation.NativeScroll
import zone.ien.navigation.NativeSearchBar
import zone.ien.navigation.nativeContentPadding
import zone.ien.navigation.rememberNativeScroll
import zone.ien.utils.ui.menu.ActionMenuItem

/**
 * 현재 native 화면의 내비게이션 바 여백을 Cupertino 구성 요소에 제공합니다.
 *
 * 기존에 바 여백이 제공된 화면에서는 해당 값을 유지하고, native 화면에서는 navigation host의 여백을 사용합니다.
 *
 * @param content 여백을 제공할 콘텐츠
 */
@Composable
public fun ProvideNativeScreenPadding(content: @Composable () -> Unit) {
    val padding = if (LocalNativeScreen.current) {
        nativeContentPadding()
    } else {
        LocalCupertinoNativeBarsPadding.current
    }
    CompositionLocalProvider(
        LocalCupertinoNativeBarsPadding provides padding,
        content = content,
    )
}

/**
 * UIKit 내비게이션 바가 있는 화면에 제목과 액션을 연결합니다.
 *
 * @param title 내비게이션 바 제목
 * @param leading 왼쪽에 표시할 사용자 지정 native 버튼
 * @param actions 오른쪽에 표시할 액션 항목
 * @param primaryAction 오른쪽 끝에 표시할 주요 액션
 * @return native 화면이면 `true`
 */
@Composable
public fun NativeScreenBar(
    title: String? = null,
    leading: List<NativeBarButton> = emptyList(),
    actions: List<ActionMenuItem> = emptyList(),
    primaryAction: ActionMenuItem.IconMenuItem? = null,
): Boolean {
    val isNativeScreen = LocalNativeScreen.current && LocalCupertinoNativeBarsPadding.current != null
    if (isNativeScreen) {
        NativeBarItems(
            title = title,
            leading = leading,
            trailing = actions.toNativeBarButtons(primaryAction),
        )
    }
    return isNativeScreen
}

/**
 * 검색 상태를 UIKit 검색창에 연결합니다.
 *
 * @param query 현재 검색어
 * @param onQueryChange 검색어 변경 콜백
 * @param placeholder 검색 입력 안내 문구
 * @return native 화면이면 `true`
 */
@Composable
public fun NativeScreenSearch(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
): Boolean {
    val isNativeScreen = LocalNativeScreen.current && LocalCupertinoNativeBarsPadding.current != null
    if (isNativeScreen) {
        NativeSearchBar(query = query, onQueryChange = onQueryChange, placeholder = placeholder)
    }
    return isNativeScreen
}

/**
 * [LazyListState]를 native 세로 스크롤과 연결합니다.
 *
 * @param state 연결할 lazy list 상태
 * @param background 스크롤 끝을 넘어 드러나는 배경색
 * @return native 화면이면 스크롤 설정, 그 외에는 `null`
 */
@Composable
public fun rememberNativeScreenScroll(
    state: LazyListState,
    background: Color = Color.Unspecified,
): NativeScroll? = if (isNativeScreen()) rememberNativeScroll(state, background) else null

/**
 * [ScrollState]를 native 세로 스크롤과 연결합니다.
 *
 * @param state 연결할 스크롤 상태
 * @param background 스크롤 끝을 넘어 드러나는 배경색
 * @return native 화면이면 스크롤 설정, 그 외에는 `null`
 */
@Composable
public fun rememberNativeScreenScroll(
    state: ScrollState,
    background: Color = Color.Unspecified,
): NativeScroll? = if (isNativeScreen()) rememberNativeScroll(state, background) else null

@Composable
private fun isNativeScreen(): Boolean =
    LocalNativeScreen.current && LocalCupertinoNativeBarsPadding.current != null

internal fun List<ActionMenuItem>.toNativeBarButtons(
    primaryAction: ActionMenuItem.IconMenuItem? = null,
): List<NativeBarButton> {
    val actions = this
    return buildList {
        actions.filter { it.visible }.forEach { action ->
            add(action.toNativeBarButton())
        }
        primaryAction?.takeIf { it.visible }?.let { action ->
            add(action.toNativeBarButton())
        }
    }
}

private fun ActionMenuItem.toNativeBarButton(): NativeBarButton = NativeBarButton(
    title = title,
    enabled = enabled,
    onClick = onClick,
)
