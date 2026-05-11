package zone.ien.utils.adaptive.screen

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi

/**
 * 현재 상단바 어댑테이션 설정을 제공하는 CompositionLocal
 *
 * @return ProvidableCompositionLocal - 상단바 어댑테이션 설정
 */
@OptIn(ExperimentalAdaptiveApi::class)
val LocalTopBarAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarAdaptation, M3TopAppBarAdaptation>.() -> Unit> = staticCompositionLocalOf { {
        material {
            isCenterAligned = true
        }
    } }

/**
 * 현재 상단바 스캐폴드 어댑테이션 설정을 제공하는 CompositionLocal
 *
 * @return ProvidableCompositionLocal - 상단바 스캐폴드 어댑테이션 설정
 */
@OptIn(ExperimentalAdaptiveApi::class)
val LocalTopBarScaffoldAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarScaffoldAdaptation, M3TopAppBarScaffoldAdaptation>.() -> Unit> = staticCompositionLocalOf { {
        material {
            isCenterAligned = true
        }
    } }

/**
 * 배경 어댑티브 설정을 제공하는 CompositionLocal
 *
 * @return ProvidableCompositionLocal - 배경 어댑티브 설정
 */
val LocalIsBackgroundAdaptive: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { true }

/**
 * 배경 그라디언트 설정을 제공하는 CompositionLocal
 *
 * @return ProvidableCompositionLocal - 배경 그라디언트 설정
 */
val LocalIsBackgroundGradient: ProvidableCompositionLocal<Boolean> = staticCompositionLocalOf { false }
