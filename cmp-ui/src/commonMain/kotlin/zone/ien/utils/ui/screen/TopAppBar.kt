package zone.ien.utils.ui.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LargeFlexibleTopAppBar
import androidx.compose.material3.MediumFlexibleTopAppBar
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.TwoRowsTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

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
    size: TopAppBarSize = TopAppBarSize.Small
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
    size: TopAppBarSize
) {
    when (size) {
        TopAppBarSize.Small -> {
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
        TopAppBarSize.Medium -> {
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
        TopAppBarSize.Large -> {
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