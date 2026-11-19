package zone.ien.utils.ui.screen

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.painter.Painter
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.asPainter
import androidx.compose.ui.graphics.Color
import zone.ien.hig.LocalCupertinoNativeBarsPadding
import zone.ien.navigation.LocalNativeNavigationBarHidden
import zone.ien.navigation.LocalNativeScreen
import zone.ien.navigation.NativeBarButton as SwiftNativeBarButton
import zone.ien.navigation.NativeMenuAction
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.more_options
import zone.ien.utils.ui.menu.splitMenuItems
import zone.ien.navigation.NativeBarItems
import zone.ien.navigation.NativeScroll
import zone.ien.navigation.NativeSearchBar
import zone.ien.navigation.nativeContentPadding
import zone.ien.navigation.rememberNativeScroll
import zone.ien.utils.ui.menu.ActionMenuItem

/**
 * 현재 native 화면의 내비게이션 바 여백을 Cupertino 구성 요소에 제공합니다.
 *
 * 네이티브 바가 숨겨진 화면은 자체 스캐폴드가 안전 영역을 처리합니다.
 * 그 외 네이티브 화면은 내비게이션 호스트의 여백을 사용합니다.
 *
 * @param content 여백을 제공할 콘텐츠
 */
@Composable
public fun ProvideNativeScreenPadding(content: @Composable () -> Unit) {
    val padding = when {
        LocalNativeNavigationBarHidden.current -> null
        LocalNativeScreen.current -> nativeContentPadding()
        else -> LocalCupertinoNativeBarsPadding.current
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
    leading: List<SwiftNativeBarButton> = emptyList(),
    actions: List<ActionMenuItem> = emptyList(),
    primaryAction: ActionMenuItem.IconMenuItem? = null,
): Boolean {
    val isNativeScreen = LocalNativeScreen.current && LocalCupertinoNativeBarsPadding.current != null
    if (isNativeScreen) {
        val images = (actions + listOfNotNull(primaryAction)).filter { it.visible }.associateWith { action ->
            val icon = when (action) {
                is ActionMenuItem.IconMenuItem -> action.icon
                is ActionMenuItem.NeverShown -> action.icon
            }
            icon?.asPainter()
        }
        NativeBarItems(
            title = title,
            leading = leading,
            trailing = actions.toNativeBarButtons(
                primaryAction = primaryAction,
                images = images,
                maxVisibleItems = with(LocalDensity.current) {
                    val width = LocalWindowInfo.current.containerSize.width.toDp().value
                    val reservedButtons = leading.size + if (primaryAction?.visible == true) 1 else 0
                    (width / 2f / 44f - reservedButtons).toInt().coerceIn(0, 3)
                },
                overflowTitle = stringResource(Res.string.more_options),
            ),
        )
    }
    return isNativeScreen
}

/** 기존 IconData로 네이티브 선행 버튼과 툴바 버튼을 구성합니다. */
@Composable
fun NativeBarButton(
    title: String,
    icon: IconData? = null,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
): SwiftNativeBarButton = SwiftNativeBarButton(
    title = title,
    image = icon?.asPainter(),
    enabled = enabled,
    onClick = onClick,
)

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
    images: Map<ActionMenuItem, Painter?> = emptyMap(),
    maxVisibleItems: Int = 3,
    overflowTitle: String = "더보기",
): List<SwiftNativeBarButton> {
    val menuItems = splitMenuItems(this, maxVisibleItems)
    return buildList {
        menuItems.alwaysShownItems.forEach { action ->
            add(action.toNativeBarButton(images[action]))
        }
        if (menuItems.overflowItems.isNotEmpty()) {
            add(SwiftNativeBarButton(
                title = overflowTitle,
                systemImage = "ellipsis",
                menu = menuItems.overflowItems.map { action ->
                    NativeMenuAction(
                        title = action.title,
                        image = images[action],
                        enabled = action.enabled,
                        badge = (action as? ActionMenuItem.IconMenuItem)?.badge ?: 0,
                        onClick = action.onClick,
                    )
                },
            ))
        }
        primaryAction?.takeIf { it.visible }?.let { action ->
            add(action.toNativeBarButton(images[action], isPrimary = true))
        }
    }
}

private fun ActionMenuItem.toNativeBarButton(image: Painter?, isPrimary: Boolean = false): SwiftNativeBarButton = SwiftNativeBarButton(
    title = title,
    enabled = enabled,
    image = image,
    badge = (this as? ActionMenuItem.IconMenuItem)?.badge ?: 0,
    isPrimary = isPrimary,
    onClick = onClick,
)

/** 세로 스크롤과 네이티브 상단 바 연결을 함께 구성합니다. 다른 플랫폼에서는 일반 verticalScroll로 동작합니다. */
fun Modifier.ienVerticalScroll(
    state: ScrollState,
    enabled: Boolean = true,
    flingBehavior: FlingBehavior? = null,
    reverseScrolling: Boolean = false,
): Modifier = composed {
    val nativeScroll = if (enabled) rememberNativeScreenScroll(state) else null
    verticalScroll(
        state = state,
        enabled = enabled && (nativeScroll?.userScrollEnabled ?: true),
        flingBehavior = flingBehavior,
        reverseScrolling = reverseScrolling,
    )
}

/** 일반 LazyColumn 옵션을 유지하며 목록 상태를 네이티브 상단 바에 연결합니다. */
@Composable
fun IenLazyColumn(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical = if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    content: LazyListScope.() -> Unit,
) {
    val nativeScroll = if (userScrollEnabled) rememberNativeScreenScroll(state) else null
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled && (nativeScroll?.userScrollEnabled ?: true),
        content = content,
    )
}
