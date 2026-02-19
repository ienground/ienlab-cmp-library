package zone.ien.utils.adaptive.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kyant.backdrop.backdrops.LayerBackdrop
import com.kyant.backdrop.backdrops.rememberLayerBackdrop
import zone.ien.hig.CupertinoTopAppBar
import zone.ien.hig.CupertinoTopAppBarColors
import zone.ien.hig.CupertinoTopAppBarDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.screen.LocalIsScrollTint
import zone.ien.utils.ui.screen.M3TopAppBar

@OptIn(
    ExperimentalAdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    adaptation: AdaptationScope<HigTopAppBarAdaptation, M3TopAppBarAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { TopAppBarAdaptation() },
        adaptationScope = adaptation,
        material = {
            M3TopAppBar(
                title = title,
                modifier = modifier,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = it.windowInsets,
                scrollBehavior = it.scrollBehavior,
                isScrollTint = it.isScrollTint,
                isCenterAligned = it.isCenterAligned,
                colors = it.colors
            )
        },
        cupertino = {
            CupertinoTopAppBar(
                title = title,
                modifier = modifier,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = it.windowInsets,
                isCenterAligned = it.isCenterAligned,
                isTransparent = it.isTransparent,
                isTranslucent = it.isTranslucent,
                colors = it.colors
            )
        }
    )
}

@Stable
@OptIn(ExperimentalMaterial3Api::class)
class M3TopAppBarAdaptation internal constructor(
    windowInsets: WindowInsets,
    scrollBehavior: TopAppBarScrollBehavior,
    isScrollTint: Boolean,
    colors: TopAppBarColors,
    isCenterAligned: Boolean = false
) {
    var windowInsets: WindowInsets by mutableStateOf(windowInsets)
    var scrollBehavior: TopAppBarScrollBehavior by mutableStateOf(scrollBehavior)
    var isScrollTint: Boolean by mutableStateOf(isScrollTint)
    var colors: TopAppBarColors by mutableStateOf(colors)
    var isCenterAligned: Boolean by mutableStateOf(isCenterAligned)
}

@Stable
class HigTopAppBarAdaptation internal constructor(
    windowInsets: WindowInsets,
    isBackgroundAdaptive: Boolean = true,
    backdrop: LayerBackdrop,
    isCenterAligned: Boolean = true,
    isTransparent: Boolean = false,
    isTranslucent: Boolean = true,
    colors: CupertinoTopAppBarColors
) {
    var windowInsets: WindowInsets by mutableStateOf(windowInsets)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
    var backdrop: LayerBackdrop by mutableStateOf(backdrop)
    var isCenterAligned: Boolean by mutableStateOf(isCenterAligned)
    var isTransparent: Boolean by mutableStateOf(isTransparent)
    var isTranslucent: Boolean by mutableStateOf(isTranslucent)
    var colors: CupertinoTopAppBarColors by mutableStateOf(colors)
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
internal class TopAppBarAdaptation: Adaptation<HigTopAppBarAdaptation, M3TopAppBarAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigTopAppBarAdaptation {
        val windowInsets = CupertinoTopAppBarDefaults.windowInsets
        val isBackgroundAdaptive = true
        val backdrop = rememberLayerBackdrop()
        val isCenterAligned = true
        val isTransparent = false
        val isTranslucent = true
        val colors = CupertinoTopAppBarDefaults.topAppBarColors()

        return remember(windowInsets, isBackgroundAdaptive, backdrop, isCenterAligned, isTransparent, isTranslucent, colors) {
            HigTopAppBarAdaptation(
                windowInsets = windowInsets,
                isBackgroundAdaptive = isBackgroundAdaptive,
                backdrop = backdrop,
                isCenterAligned = isCenterAligned,
                isTransparent = isTransparent,
                isTranslucent = isTranslucent,
                colors = colors
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun rememberMaterialAdaptation(): M3TopAppBarAdaptation {
        val windowInsets = TopAppBarDefaults.windowInsets
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
        val isScrollTint = LocalIsScrollTint.current
        val colors = TopAppBarDefaults.topAppBarColors()
        val isCenterAligned = false

        return remember(windowInsets, scrollBehavior, isScrollTint, colors, isCenterAligned) {
            M3TopAppBarAdaptation(
                windowInsets = windowInsets,
                scrollBehavior = scrollBehavior,
                isScrollTint = isScrollTint,
                colors = colors,
                isCenterAligned = isCenterAligned
            )
        }
    }
}

