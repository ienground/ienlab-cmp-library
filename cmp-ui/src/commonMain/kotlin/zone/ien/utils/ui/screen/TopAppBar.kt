package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.composite.IenTopBar
import zone.ien.utils.ui.components.composite.IenTopBarTitleAlignment

/**
 * M3TopAppBar은 상단 앱 바를 표시하기 위한 컴포저블입니다.
 *
 * @param title 제목
 * @param subtitle 부제목
 * @param modifier 적용할 Modifier
 * @param navigationIcon 네비게이션 아이콘
 * @param actions 액션 버튼들
 * @param windowInsets 윈도우 인셋
 * @param scrollBehavior 스크롤 동작
 * @param isScrollTint 스크롤 tint 여부
 * @param isCenterAligned 중앙 정렬 여부
 * @param colors 색상
 * @param size 크기
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TopAppBar(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    isScrollTint: Boolean = LocalIsScrollTint.current,
    isCenterAligned: Boolean = false,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    size: TopBarSize = TopBarSize.Small
) {
    IenTopBar(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        titleAlignment = if (isCenterAligned) IenTopBarTitleAlignment.Center else IenTopBarTitleAlignment.Start,
        contentPadding = when (size) {
            TopBarSize.Small,
            TopBarSize.Medium,
            TopBarSize.Large -> PaddingValues(horizontal = 16.dp)
        },
        contentHeight = when (size) {
            TopBarSize.Small -> 64.dp
            TopBarSize.Medium -> 80.dp
            TopBarSize.Large -> 96.dp
        },
    )
}
