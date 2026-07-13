package zone.ien.utils.adaptive.screen

import androidx.compose.foundation.ScrollState
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
val LocalTopBarAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarAdaptation, IenTopAppBarAdaptation>.() -> Unit> = staticCompositionLocalOf { {
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
val LocalTopBarScaffoldAdaptation: ProvidableCompositionLocal<AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit> = staticCompositionLocalOf { {
        material {
            isCenterAligned = true
        }
    } }

/**
 * 현재 상단바 스캐폴드가 콘텐츠와 공유하는 기본 스크롤 상태
 *
 * @return ProvidableCompositionLocal - 콘텐츠가 별도 상태를 받지 않았을 때 사용할 스크롤 상태
 */
val LocalTopBarScaffoldScrollState: ProvidableCompositionLocal<ScrollState?> = staticCompositionLocalOf { null }

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
