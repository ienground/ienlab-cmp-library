package zone.ien.utils.adaptive.utils

import androidx.compose.runtime.Composable
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.screen.HigTopAppBarScaffoldAdaptation
import zone.ien.utils.adaptive.screen.IenTopAppBarScaffoldAdaptation
import zone.ien.utils.ui.screen.TopBarMode

/**
 * Surface 색상의 상단 앱 바 적응형 설정을 반환하는 함수
 * 
 * Material과 Cupertino 플랫폼에 따라 다르게 동작하는 상단 앱 바의 적응형 설정을 제공합니다.
 * 
 * @param backdrop 상단 앱 바에 적용할 배경 레이어
 * @param mode 상단바 표시 방식
 * @param isCenterAligned 타이틀을 중앙 정렬할지 여부
 * @return 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun getSurfaceTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    mode: TopBarMode = TopBarMode.Static,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        this.isCenterAligned = isCenterAligned
        this.mode = mode
    }
    cupertino {
        this.backdrop = backdrop
        this.mode = mode
    }
}

/**
 * SurfaceContainer 색상의 상단 앱 바 적응형 설정을 반환하는 함수
 * 
 * Material과 Cupertino 플랫폼에 따라 다르게 동작하는 상단 앱 바의 적응형 설정을 제공합니다.
 * 
 * @param backdrop 상단 앱 바에 적용할 배경 레이어
 * @param mode 상단바 표시 방식
 * @param isCenterAligned 타이틀을 중앙 정렬할지 여부
 * @return 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun getSurfaceContainerTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    mode: TopBarMode = TopBarMode.Static,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        this.isCenterAligned = isCenterAligned
        this.mode = mode
    }
    cupertino {
        this.backdrop = backdrop
        this.mode = mode
    }
}

/**
 * 투명한 색상의 상단 앱 바 적응형 설정을 반환하는 함수
 * 
 * Material과 Cupertino 플랫폼에 따라 다르게 동작하는 상단 앱 바의 적응형 설정을 제공합니다.
 * 
 * @param backdrop 상단 앱 바에 적용할 배경 레이어
 * @param mode 상단바 표시 방식
 * @param isCenterAligned 타이틀을 중앙 정렬할지 여부
 * @return 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun getNoTintTopAppBarAdaptation(
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    mode: TopBarMode = TopBarMode.Static,
    isCenterAligned: Boolean = true
): AdaptationScope<HigTopAppBarScaffoldAdaptation, IenTopAppBarScaffoldAdaptation>.() -> Unit = {
    material {
        isScrollTint = false
        this.isCenterAligned = isCenterAligned
        this.mode = mode
    }
    cupertino {
        this.backdrop = backdrop
        this.mode = mode
    }
}
