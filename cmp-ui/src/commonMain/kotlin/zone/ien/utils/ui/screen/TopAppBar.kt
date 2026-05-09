package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    TopAppBarImpl(
        title = title,
        subtitle = subtitle,
        isCenterAligned = isCenterAligned,
        colors = colors.let { if (isScrollTint) it else it.copy(scrolledContainerColor = it.containerColor) },
        modifier = modifier,
        navigationIcon = navigationIcon,
        actions = actions,
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        size = size
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopAppBarImpl(
    title: @Composable () -> Unit,
    subtitle: @Composable (() -> Unit)?,
    isCenterAligned: Boolean,
    colors: TopAppBarColors,
    modifier: Modifier,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable (RowScope.() -> Unit),
    windowInsets: WindowInsets,
    scrollBehavior: TopAppBarScrollBehavior?,
    size: TopBarSize
) {
    when (size) {
        TopBarSize.Small -> {
            TopAppBar(
                title = title,
                modifier = modifier,
                subtitle = subtitle ?: {},
                navigationIcon = navigationIcon,
                actions = actions,
                titleHorizontalAlignment = if (isCenterAligned) Alignment.CenterHorizontally else Alignment.Start,
                windowInsets = windowInsets,
                colors = colors,
                scrollBehavior = scrollBehavior,
            )
        }
        TopBarSize.Medium -> {
            MediumFlexibleTopAppBar(
                title = title,
                modifier = modifier,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = actions,
                titleHorizontalAlignment = if (isCenterAligned) Alignment.CenterHorizontally else Alignment.Start,
                windowInsets = windowInsets,
                colors = colors,
                scrollBehavior = scrollBehavior
            )
        }
        TopBarSize.Large -> {
            LargeFlexibleTopAppBar(
                title = title,
                modifier = modifier,
                subtitle = subtitle,
                navigationIcon = navigationIcon,
                actions = actions,
                titleHorizontalAlignment = if (isCenterAligned) Alignment.CenterHorizontally else Alignment.Start,
                windowInsets = windowInsets,
                colors = colors,
                scrollBehavior = scrollBehavior
            )
        }
    }
}