package zone.ien.utils.adaptive.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.kyant.backdrop.Backdrop
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.hig.CupertinoLargeFloatingActionButton
import zone.ien.hig.CupertinoLiquidButtonColors
import zone.ien.hig.CupertinoLiquidButtonDefaults
import zone.ien.hig.CupertinoMediumFloatingActionButton
import zone.ien.hig.CupertinoSmallFloatingActionButton
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenFab
import zone.ien.utils.ui.interactive.IenFabSize

/**
 * 적응형 소형 플로팅 액션 버튼 컴포저블
 *
 * @param onClick 버튼 클릭 시 실행할 함수
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응 스타일 설정 함수
 * @param content 버튼 내부에 표시할 콘텐츠
 * @return 소형 플로팅 액션 버튼 컴포저블
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveSmallFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { SmallFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenFab(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoSmallFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

/**
 * 적응형 중형 플로팅 액션 버튼 컴포저블
 *
 * @param onClick 버튼 클릭 시 실행할 함수
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응 스타일 설정 함수
 * @param content 버튼 내부에 표시할 콘텐츠
 * @return 중형 플로팅 액션 버튼 컴포저블
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveMediumFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { MediumFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenFab(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoMediumFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

/**
 * 적응형 대형 플로팅 액션 버튼 컴포저블
 *
 * @param onClick 버튼 클릭 시 실행할 함수
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param adaptation 적응 스타일 설정 함수
 * @param content 버튼 내부에 표시할 콘텐츠
 * @return 대형 플로팅 액션 버튼 컴포저블
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLargeFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    adaptation: AdaptationScope<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>.() -> Unit = {},
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        adaptation = remember { LargeFloatingActionButtonAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenFab(
                onClick = onClick,
                modifier = modifier,
                size = it.size,
                variant = it.variant,
                tone = it.tone,
                state = it.state,
                shape = it.shape,
                interactionSource = it.interactionSource,
                content = content
            )
        },
        cupertino = {
            CupertinoLargeFloatingActionButton(
                onClick = onClick,
                modifier = modifier,
                colors = it.colors,
                shape = it.shape,
                interactionSource = it.interactionSource,
                backdrop = it.backdrop,
                isBackgroundAdaptive = it.isBackgroundAdaptive,
                content = content
            )
        }
    )
}

/**
 * IEN 플로팅 액션 버튼 적응 데이터 클래스
 *
 * @param size 버튼 크기
 * @param variant 버튼 표현 방식
 * @param tone 버튼 의미 색상
 * @param state 버튼 상태
 * @param shape 모양
 * @param interactionSource 상호작용 소스
 * @return IEN 플로팅 액션 버튼 적응 데이터 객체
 */
class IenFloatingActionButtonAdaptation internal constructor(
    size: IenFabSize,
    variant: IenButtonVariant,
    tone: IenSemanticTone,
    state: IenButtonState,
    shape: Shape,
    interactionSource: MutableInteractionSource
) {
    var size: IenFabSize by mutableStateOf(size)
    var variant: IenButtonVariant by mutableStateOf(variant)
    var tone: IenSemanticTone by mutableStateOf(tone)
    var state: IenButtonState by mutableStateOf(state)
    var shape: Shape by mutableStateOf(shape)
    var interactionSource: MutableInteractionSource by mutableStateOf(interactionSource)
}

/**
 * HIG 플로팅 액션 버튼 적응 데이터 클래스
 *
 * @param colors 버튼 색상
 * @param shape 모양
 * @param interactionSource 상호작용 소스
 * @param backdrop Backdrop 컴포넌트
 * @param isBackgroundAdaptive 배경 적응 여부
 * @return HIG 플로팅 액션 버튼 적응 데이터 객체
 */
class HigFloatingActionButtonAdaptation internal constructor(
    colors: CupertinoLiquidButtonColors,
    shape: Shape,
    interactionSource: MutableInteractionSource,
    backdrop: Backdrop,
    isBackgroundAdaptive: Boolean = true
) {
    var colors: CupertinoLiquidButtonColors by mutableStateOf(colors)
    var shape: Shape by mutableStateOf(shape)
    var interactionSource: MutableInteractionSource by mutableStateOf(interactionSource)
    var backdrop: Backdrop by mutableStateOf(backdrop)
    var isBackgroundAdaptive: Boolean by mutableStateOf(isBackgroundAdaptive)
}

/**
 * 소형 플로팅 액션 버튼 적응 클래스
 *
 * @return 소형 플로팅 액션 버튼 적응 객체
 */
@OptIn(ExperimentalAdaptiveApi::class)
private class SmallFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenFloatingActionButtonAdaptation {
        val size = IenFabSize.Small
        val variant = IenButtonVariant.Fill
        val tone = IenSemanticTone.Brand
        val state = IenButtonState()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }

        return remember(size, variant, tone, state, shape, interactionSource) {
            IenFloatingActionButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                interactionSource = interactionSource
            )
        }
    }
}

/**
 * 중형 플로팅 액션 버튼 적응 클래스
 *
 * @return 중형 플로팅 액션 버튼 적응 객체
 */
@OptIn(ExperimentalAdaptiveApi::class)
private class MediumFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenFloatingActionButtonAdaptation {
        val size = IenFabSize.Regular
        val variant = IenButtonVariant.Fill
        val tone = IenSemanticTone.Brand
        val state = IenButtonState()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }

        return remember(size, variant, tone, state, shape, interactionSource) {
            IenFloatingActionButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                interactionSource = interactionSource
            )
        }
    }
}

/**
 * 대형 플로팅 액션 버튼 적응 클래스
 *
 * @return 대형 플로팅 액션 버튼 적응 객체
 */
@OptIn(ExperimentalAdaptiveApi::class)
private class LargeFloatingActionButtonAdaptation: Adaptation<HigFloatingActionButtonAdaptation, IenFloatingActionButtonAdaptation>() {
    @Composable
    override fun rememberCupertinoAdaptation(): HigFloatingActionButtonAdaptation {
        val colors = CupertinoLiquidButtonDefaults.glassProminentButtonColors()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }
        val backdrop = rememberDefaultBackdrop()
        val isBackgroundAdaptive = true

        return remember(colors, shape, interactionSource, backdrop, isBackgroundAdaptive) {
            HigFloatingActionButtonAdaptation(
                colors = colors,
                shape = shape,
                interactionSource = interactionSource,
                backdrop = backdrop,
                isBackgroundAdaptive = isBackgroundAdaptive
            )
        }
    }

    @Composable
    override fun rememberMaterialAdaptation(): IenFloatingActionButtonAdaptation {
        val size = IenFabSize.Large
        val variant = IenButtonVariant.Fill
        val tone = IenSemanticTone.Brand
        val state = IenButtonState()
        val shape = CircleShape
        val interactionSource = remember { MutableInteractionSource() }

        return remember(size, variant, tone, state, shape, interactionSource) {
            IenFloatingActionButtonAdaptation(
                size = size,
                variant = variant,
                tone = tone,
                state = state,
                shape = shape,
                interactionSource = interactionSource
            )
        }
    }
}
