package zone.ien.utils.adaptive.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.HigTopAppBarScaffoldAdaptation
import zone.ien.utils.adaptive.screen.M3TopAppBarScaffoldAdaptation
import zone.ien.utils.ui.screen.TopBarSize

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun getSurfaceTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    showNavTitle: Boolean = false,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
        this.isCenterAligned = isCenterAligned

        if (showNavTitle) {
            size = TopBarSize.Medium
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        }
    }
    cupertino {
        this.backdrop = backdrop
        this.showNavTitle = showNavTitle
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun getSurfaceContainerTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    showNavTitle: Boolean = false,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
        this.isCenterAligned = isCenterAligned

        if (showNavTitle) {
            size = TopBarSize.Medium
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        }
    }
    cupertino {
        this.backdrop = backdrop
        this.showNavTitle = showNavTitle
    }
}

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun getNoTintTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    showNavTitle: Boolean = false,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        isScrollTint = false
        this.isCenterAligned = isCenterAligned

        if (showNavTitle) {
            size = TopBarSize.Medium
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        }
    }
    cupertino {
        this.backdrop = backdrop
        this.showNavTitle = showNavTitle
    }
}