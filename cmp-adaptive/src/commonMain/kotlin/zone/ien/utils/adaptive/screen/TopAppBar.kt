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
import zone.ien.hig.utils.rememberDefaultLayerBackdrop
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
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                isBackgroundGradient = it.isBackgroundGradient,
                backdrop = it.backdrop,
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
    isCenterAligned: Boolean = true,
    isBackgroundAdaptive: Boolean = true,
    isBackgroundGradient: Boolean = false,
    backdrop: LayerBackdrop,
    colors: CupertinoTopAppBarColors
) {
    var windowInsets: WindowInsets by mutableStateOf(windowInsets)
    var isCenterAligned: Boolean by mutableStateOf(isCenterAligned)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
    var isBackgroundGradient: Boolean by mutableStateOf(isBackgroundGradient)
    var backdrop: LayerBackdrop by mutableStateOf(backdrop)
    var colors: CupertinoTopAppBarColors by mutableStateOf(colors)
}

@OptIn(ExperimentalAdaptiveApi::class)
@Stable
internal class TopAppBarAdaptation: Adaptation<HigTopAppBarAdaptation, M3TopAppBarAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigTopAppBarAdaptation {
        val windowInsets = CupertinoTopAppBarDefaults.windowInsets
        val isCenterAligned = true
        val isBackgroundAdaptive = LocalIsBackgroundAdaptive.current
        val isBackgroundGradient = LocalIsBackgroundGradient.current
        val backdrop = rememberDefaultLayerBackdrop()
        val colors = CupertinoTopAppBarDefaults.topAppBarColors()

        return remember(windowInsets, backdrop, isCenterAligned, isBackgroundAdaptive, isBackgroundGradient, colors) {
            HigTopAppBarAdaptation(
                windowInsets = windowInsets,
                isCenterAligned = isCenterAligned,
                isBackgroundAdaptive = isBackgroundAdaptive,
                isBackgroundGradient = isBackgroundGradient,
                backdrop = backdrop,
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
        val isCenterAligned = true

        return remember(windowInsets, isScrollTint, colors, isCenterAligned) {
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

