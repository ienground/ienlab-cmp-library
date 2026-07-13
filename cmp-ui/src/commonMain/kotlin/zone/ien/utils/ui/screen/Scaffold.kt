package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import zone.ien.utils.ui.components.composite.IenScaffoldContentEdge
import zone.ien.utils.ui.components.composite.IenScaffold
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.menu.ActionMenuItem
import zone.ien.utils.ui.menu.M3ActionsMenu

/**
 * M3TopAppBarScaffold는 상단 앱 바를 가진 스크래프트를 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param topBarModifier 상단 바에 적용할 Modifier
 * @param title 제목
 * @param subtitle 부제목
 * @param showTopBar 상단 바 표시 여부
 * @param navigationIcon 네비게이션 아이콘
 * @param actions 액션 버튼들
 * @param topBarWindowInsets 상단 바 윈도우 인셋
 * @param bottomBar 하단 바
 * @param snackbarHost 스낵바 호스트
 * @param floatingActionButton 플로팅 액션 버튼
 * @param floatingActionButtonPosition 플로팅 액션 버튼 위치
 * @param topAppBarColors 상단 바 색상
 * @param isCenterAligned 중앙 정렬 여부
 * @param scaffoldContainerColor 스크래프트 컨테이너 색상
 * @param scaffoldContentColor 스크래프트 내용 색상
 * @param contentWindowInsets 내용 윈도우 인셋
 * @param isScrollTint 스크롤 tint 여부
 * @param size 크기
 * @param scrollBehavior 스크롤 동작
 * @param content 내용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TopAppBarScaffold(
    modifier: Modifier = Modifier,
    topBarModifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    subtitle: @Composable (() -> Unit)? = null,
    showTopBar: Boolean = true,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable (RowScope.() -> Unit))? = null,
    topBarWindowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    isCenterAligned: Boolean = LocalIsM3TopBarCenterAligned.current,
    scaffoldContainerColor: Color = IenTheme.colors.background,
    scaffoldContentColor: Color = IenTheme.colors.textPrimary,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(),
    isScrollTint: Boolean = LocalIsScrollTint.current,
    size: TopBarSize = LocalM3TopBarSize.current,
    scrollBehavior: TopAppBarScrollBehavior = if (size == TopBarSize.Small) TopAppBarDefaults.pinnedScrollBehavior() else TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit
) {
    IenScaffold(
        modifier = modifier,
        topBar = {
            if (showTopBar) {
                M3TopAppBar(
                    title = title,
                    subtitle = subtitle,
                    navigationIcon = navigationIcon,
                    actions = actions,
                    windowInsets = topBarWindowInsets,
                    colors = topAppBarColors,
                    isCenterAligned = isCenterAligned,
                    scrollBehavior = scrollBehavior,
                    isScrollTint = isScrollTint,
                    size = size,
                    modifier = topBarModifier
                )
            } else {
                Spacer(modifier = Modifier.statusBarsPadding())
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floating = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentWindowInsets = contentWindowInsets,
        containerColor = scaffoldContainerColor,
        contentColor = scaffoldContentColor,
        contentEdge = contentEdge,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TopAppBarScaffold(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    subtitle: @Composable (() -> Unit)? = null,
    topBarModifier: Modifier = Modifier,
    showTopBar: Boolean = true,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: List<ActionMenuItem> = listOf(),
    topBarWindowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    bottomBar: (@Composable () -> Unit)? = null,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.Center,
    topAppBarColors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    isCenterAligned: Boolean = LocalIsM3TopBarCenterAligned.current,
    scaffoldContainerColor: Color = IenTheme.colors.background,
    scaffoldContentColor: Color = IenTheme.colors.textPrimary,
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    contentEdge: IenScaffoldContentEdge = IenScaffoldContentEdge(),
    isScrollTint: Boolean = LocalIsScrollTint.current,
    size: TopBarSize = LocalM3TopBarSize.current,
    scrollBehavior: TopAppBarScrollBehavior = if (size == TopBarSize.Small) TopAppBarDefaults.pinnedScrollBehavior() else TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    M3TopAppBarScaffold(
        modifier = modifier,
        title = title,
        subtitle = subtitle,
        topBarModifier = topBarModifier,
        showTopBar = showTopBar,
        navigationIcon = navigationIcon,
        actions = actions.takeIf { it.isNotEmpty() }?.let {
            {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    M3ActionsMenu(
                        items = it,
                        isOpen = menuExpanded,
                        closeDropdown = { menuExpanded = false },
                        onToggleOverflow = { menuExpanded = !menuExpanded },
                        maxVisibleItems = 5
                    )
                }
            }
        },
        topBarWindowInsets = topBarWindowInsets,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        topAppBarColors = topAppBarColors,
        isCenterAligned = isCenterAligned,
        scaffoldContainerColor = scaffoldContainerColor,
        scaffoldContentColor = scaffoldContentColor,
        contentWindowInsets = contentWindowInsets,
        contentEdge = contentEdge,
        scrollBehavior = scrollBehavior,
        isScrollTint = isScrollTint,
        size = size,
        content = content
    )
}
