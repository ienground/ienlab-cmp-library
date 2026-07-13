package zone.ien.utils.adaptive.screen

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.hig.CupertinoTopAppBar
import zone.ien.hig.CupertinoTopAppBarColors
import zone.ien.hig.CupertinoTopAppBarDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.screen.LocalIsHigTopBarCenterAligned
import zone.ien.utils.ui.screen.LocalIsM3TopBarCenterAligned
import zone.ien.utils.ui.screen.LocalIsScrollTint
import zone.ien.utils.ui.screen.LocalM3TopBarSize
import zone.ien.utils.ui.screen.IenTopAppBar
import zone.ien.utils.ui.screen.TopBarSize

/**
 * 적응형 상단바 컴포저블
 *
 * @param title 제목
 * @param modifier 레이아웃 수정자
 * @param navigationIcon 뒤로가기 아이콘
 * @param actions 액션 버튼들
 * @param adaptation 어댑테이션 설정
 */
@OptIn(
    ExperimentalAdaptiveApi::class,
    ExperimentalMaterial3Api::class,
    ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: (@Composable () -> Unit)? = null,
    actions: (@Composable (RowScope.() -> Unit))? = null,
    adaptation: AdaptationScope<HigTopAppBarAdaptation, IenTopAppBarAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { TopAppBarAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenTopAppBar(
                title = title,
                modifier = modifier,
                navigationIcon = navigationIcon,
                actions = actions,
                windowInsets = it.windowInsets,
                isScrollTint = it.isScrollTint,
                isCenterAligned = it.isCenterAligned,
                size = it.size
            )
        },
        cupertino = {
            CupertinoTopAppBar(
                title = title,
                modifier = modifier,
                navigationIcon = { navigationIcon?.invoke() },
                actions = { actions?.invoke(this) },
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

/**
 * M3 상단바 어댑테이션 클래스
 *
 * @param windowInsets 윈도우 인셋
 * @param isScrollTint 스크롤 틴트 여부
 * @param isCenterAligned 중앙 정렬 여부
 * @param size 상단바 크기
 */
@Stable
@OptIn(ExperimentalMaterial3Api::class)
class IenTopAppBarAdaptation internal constructor(
    windowInsets: WindowInsets,
    isScrollTint: Boolean,
    isCenterAligned: Boolean,
    size: TopBarSize
) {
    var windowInsets by mutableStateOf(windowInsets)
    var isScrollTint by mutableStateOf(isScrollTint)
    var isCenterAligned by mutableStateOf(isCenterAligned)
    var size by mutableStateOf(size)
}

/**
 * HIG 상단바 어댑테이션 클래스
 *
 * @param windowInsets 윈도우 인셋
 * @param isCenterAligned 중앙 정렬 여부
 * @param isBackgroundAdaptive 배경 어댑티브 여부. 기본값은 true
 * @param isBackgroundGradient 배경 그라디언트 여부. 기본값은 false
 * @param backdrop 뒷배경
 * @param colors 상단바 색상
 */
@Stable
class HigTopAppBarAdaptation internal constructor(
    windowInsets: WindowInsets,
    isCenterAligned: Boolean,
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
internal class TopAppBarAdaptation: Adaptation<HigTopAppBarAdaptation, IenTopAppBarAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigTopAppBarAdaptation {
        val windowInsets = CupertinoTopAppBarDefaults.windowInsets
        val isCenterAligned = LocalIsHigTopBarCenterAligned.current
        val isBackgroundAdaptive = LocalIsBackgroundAdaptive.current
        val isBackgroundGradient = LocalIsBackgroundGradient.current
        val backdrop = rememberDefaultBackdrop()
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
    override fun rememberMaterialAdaptation(): IenTopAppBarAdaptation {
        val windowInsets = TopAppBarDefaults.windowInsets
        val isScrollTint = LocalIsScrollTint.current
        val isCenterAligned = LocalIsM3TopBarCenterAligned.current
        val size = LocalM3TopBarSize.current

        return remember(windowInsets, isScrollTint, isCenterAligned, size) {
            IenTopAppBarAdaptation(
                windowInsets = windowInsets,
                isScrollTint = isScrollTint,
                isCenterAligned = isCenterAligned,
                size = size
            )
        }
    }
}
