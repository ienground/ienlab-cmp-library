package zone.ien.utils.adaptive.screen

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi

@OptIn(ExperimentalAdaptiveApi::class)
val LocalTopBarAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarAdaptation, M3TopAppBarAdaptation>.() -> Unit> = staticCompositionLocalOf { {
        material {
            isCenterAligned = true
        }
    } }

@OptIn(ExperimentalAdaptiveApi::class)
val LocalTopBarScaffoldAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit> = staticCompositionLocalOf { {
        material {
            isCenterAligned = true
        }
    } }