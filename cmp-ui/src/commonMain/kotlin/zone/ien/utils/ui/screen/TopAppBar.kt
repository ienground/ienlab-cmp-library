package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.screen.IenTopBar
import zone.ien.utils.ui.screen.IenTopBarTitleAlignment

/**
 * IenTopAppBar은 상단 앱 바를 표시하기 위한 컴포저블입니다.
 *
 * @param title 제목
 * @param subtitle 부제목
 * @param modifier 적용할 Modifier
 * @param navigationIcon 네비게이션 아이콘
 * @param actions 액션 버튼들
 * @param windowInsets 윈도우 인셋
 * @param isScrollTint 스크롤 tint 여부
 * @param isCenterAligned 중앙 정렬 여부
 * @param mode 상단 바 표시 방식
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenTopAppBar(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable (RowScope.() -> Unit))? = null,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    isScrollTint: Boolean = LocalIsScrollTint.current,
    isCenterAligned: Boolean = false,
    mode: TopBarMode = TopBarMode.Static,
) {
    IenTopBar(
        title = title,
        subtitle = subtitle,
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        titleAlignment = if (isCenterAligned) IenTopBarTitleAlignment.Center else IenTopBarTitleAlignment.Start,
        contentPadding = when (mode) {
            TopBarMode.Static,
            TopBarMode.Expanded -> PaddingValues(horizontal = 16.dp)
        },
        contentHeight = when (mode) {
            TopBarMode.Static,
            TopBarMode.Expanded -> 64.dp
        },
    )
}
