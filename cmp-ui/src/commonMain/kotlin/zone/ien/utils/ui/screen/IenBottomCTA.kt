package zone.ien.utils.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButtonContainer
import zone.ien.utils.ui.interactive.IenButtonDefault
import zone.ien.utils.ui.interactive.buttonHeight
import zone.ien.utils.ui.interactive.buttonPadding
import zone.ien.utils.ui.primitives.IenLoaderPrimitive
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.cupertinoScaffoldBottomBar
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.primitives.IenProvideTextStyle

/**
 * 화면 하단 고정 CTA 버튼 영역의 배경 처리 스타일을 정의하는 열거형 클래스입니다.
 */
enum class IenBottomCTABackground {
    Default,
    None,
}

/**
 * 화면 하단 고정 CTA 버튼의 등장 애니메이션을 정의하는 열거형 클래스입니다.
 */
enum class IenBottomCTAAnimation {
    Slide,
    Fade,
    Scale,
}

/**
 * 하단 CTA 버튼이 화면 진입 후 특정 딜레이 뒤에 나타나도록 설정하기 위한 데이터 클래스입니다.
 *
 * @property animation 적용할 등장 애니메이션 종류 ([IenBottomCTAAnimation])
 * @property delayMillis 등장 지연 시간 (밀리초 단위)
 */
@Immutable
data class IenBottomCTAShowAfterDelay(
    val animation: IenBottomCTAAnimation = IenBottomCTAAnimation.Slide,
    val delayMillis: Int = 1_000,
)

private enum class CtaIconAreaState {
    None,
    Icon,
    Spinner,
}

@Composable
internal fun IenBottomCTAButtonContent(
    text: String,
    loading: Boolean,
    icon: (@Composable () -> Unit)?,
) {
    if (isIenSideBarLayout) {
        IenProvideTextStyle(IenTheme.typography.body2, LocalContentColor.current) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                if (loading || icon != null) {
                    Box(Modifier.size(IenTheme.icon.md), contentAlignment = Alignment.Center) {
                        if (loading) IenLoaderPrimitive(color = LocalContentColor.current)
                        else icon?.invoke()
                    }
                }
                Text(text, textAlign = TextAlign.Center, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        return
    }
    val fastMillis = IenTheme.motion.fastMillis
    val standardEasing = IenTheme.motion.standardEasing

    val targetState = when {
        loading -> CtaIconAreaState.Spinner
        icon != null -> CtaIconAreaState.Icon
        else -> CtaIconAreaState.None
    }

    IenProvideTextStyle(IenTheme.typography.body1, LocalContentColor.current) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedContent(
                targetState = targetState,
                transitionSpec = {
                    val isExpandingOrShrinking = initialState == CtaIconAreaState.None || targetState == CtaIconAreaState.None
                    if (isExpandingOrShrinking) {
                        (fadeIn(tween(fastMillis, easing = standardEasing)) +
                            expandHorizontally(
                                animationSpec = tween(fastMillis, easing = standardEasing),
                                expandFrom = Alignment.CenterHorizontally,
                            )
                        ).togetherWith(
                            fadeOut(tween(fastMillis, easing = standardEasing)) +
                                shrinkHorizontally(
                                    animationSpec = tween(fastMillis, easing = standardEasing),
                                    shrinkTowards = Alignment.CenterHorizontally,
                                )
                        )
                    } else {
                        (fadeIn(tween(fastMillis, easing = standardEasing)) +
                            scaleIn(tween(fastMillis, easing = standardEasing), initialScale = 0.8f)
                        ).togetherWith(
                            fadeOut(tween(fastMillis, easing = standardEasing)) +
                                scaleOut(tween(fastMillis, easing = standardEasing), targetScale = 0.8f)
                        )
                    }
                },
                contentAlignment = Alignment.Center,
                label = "IenBottomCTAIconArea",
            ) { state ->
                when (state) {
                    CtaIconAreaState.None -> {
                        Spacer(modifier = Modifier.size(IenTheme.spacing.none))
                    }
                    CtaIconAreaState.Icon -> {
                        Box(
                            modifier = Modifier
                                .padding(end = IenTheme.spacing.xs)
                                .size(IenTheme.icon.md),
                            contentAlignment = Alignment.Center,
                        ) {
                            icon?.invoke()
                        }
                    }
                    CtaIconAreaState.Spinner -> {
                        Box(
                            modifier = Modifier
                                .padding(end = IenTheme.spacing.xs)
                                .size(IenTheme.icon.md),
                            contentAlignment = Alignment.Center,
                        ) {
                            IenLoaderPrimitive(color = LocalContentColor.current)
                        }
                    }
                }
            }
            Text(text)
        }
    }
}

/**
 * 화면 하단에 고정되거나 스크롤 시 사라지는 등의 동작을 지원하는 기본 단일 CTA(Call To Action) 버튼 컴포저블입니다.
 * Duo의 오른쪽 바에서는 아이콘 아래에 텍스트를 세로로 쌓은 pill 형태의 버튼을 사용합니다.
 * 액세서리가 있으면 넓은 하단 영역을 유지하고 버튼만 오른쪽 시스템 영역에 정렬합니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 버튼 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param state 버튼 상태 ([IenButtonState])
 * @param icon 버튼 왼쪽에 표시할 아이콘 컴포저블
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 * @param background 하단 바 배경 스타일 ([IenBottomCTABackground])
 * @param hasSafeAreaPadding 하단 네비게이션 바 등의 세이프 에어리어 패딩을 계산하여 적용할지 여부
 * @param hasPaddingBottom 하단 여백 추가 여부
 * @param fixed 화면 하단에 상시 고정할지 여부
 * @param fixedAboveKeyboard 소프트 키보드가 올라왔을 때 키보드 상단에 밀어올려 고정할지 여부
 * @param takeSpace 레이아웃에서 하단 고정 영역만큼 여백 공간을 차지할지 여부
 * @param show 버튼의 가시성 상태 제어용 플래그
 * @param showAfterDelay 지정된 지연 시간 이후에 버튼을 드러낼지 설정
 * @param hideOnScroll 스크롤 시 버튼을 자동으로 숨길지 여부
 * @param hideOnScrollDistanceThreshold 스크롤 감지 시 버튼이 사라지기 시작하는 스크롤 임계치
 * @param scrollDelta 현재 스크롤 변화량 값
 * @param topAccessory 버튼 위쪽에 렌더링될 추가 컴포저블
 * @param bottomAccessory 버튼 아래쪽에 렌더링될 추가 컴포저블
 */
@Composable
fun IenBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    fixedAboveKeyboard: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenBottomCTAContainer(
        modifier = modifier,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        fixedAboveKeyboard = fixedAboveKeyboard,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    ) {
        IenButtonContainer(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = IenButtonSize.Large.buttonHeight()),
            variant = variant,
            tone = tone,
            state = state,
            shape = ContinuousRoundedRectangle(if (isIenSideBarLayout) IenTheme.radius.full else IenTheme.radius.default),
            contentPadding = if (isIenSideBarLayout) PaddingValues(
                horizontal = IenTheme.spacing.xxs,
                vertical = IenTheme.spacing.xs,
            ) else IenButtonSize.Large.buttonPadding(),
            colors = IenButtonDefault.colors(variant = variant, tone = tone),
            scalePressed = 0.975f,
        ) {
            IenBottomCTAButtonContent(
                text = text,
                loading = state.loading,
                icon = icon,
            )
        }
    }
}

/**
 * 화면 하단에 나란히 배치되는 주(Primary) 버튼과 부(Secondary) 버튼으로 구성된 듀얼 CTA 컴포저블입니다.
 *
 * @param primaryText 주 버튼 텍스트
 * @param onPrimaryClick 주 버튼 클릭 콜백
 * @param primaryButtonWeight 주 버튼의 상대 너비 비율
 * @param secondaryText 부 버튼 텍스트
 * @param onSecondaryClick 부 버튼 클릭 콜백
 * @param secondaryButtonWeight 부 버튼의 상대 너비 비율
 * @param modifier 적용할 Modifier
 * @param background 하단 바 배경 스타일 ([IenBottomCTABackground])
 * @param hasSafeAreaPadding 세이프 에어리어 패딩 계산 적용 여부
 * @param hasPaddingBottom 하단 여백 추가 여부
 * @param fixed 화면 하단 고정 여부
 * @param takeSpace 레이아웃 공간 차지 여부
 * @param show 가시성 제어 플래그
 * @param showAfterDelay 지연 등장 설정
 * @param hideOnScroll 스크롤 시 숨김 여부
 * @param hideOnScrollDistanceThreshold 스크롤 감지 임계치
 * @param scrollDelta 스크롤 변화량
 * @param topAccessory 추가 상단 컴포저블
 * @param bottomAccessory 추가 하단 컴포저블
 */
@Composable
fun IenDoubleBottomCTA(
    primaryText: String,
    onPrimaryClick: () -> Unit,
    secondaryText: String,
    onSecondaryClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryState: IenButtonState = IenButtonState(),
    secondaryState: IenButtonState = IenButtonState(),
    primaryIcon: (@Composable () -> Unit)? = null,
    secondaryIcon: (@Composable () -> Unit)? = null,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
    primaryButtonWeight: Float = 1f,
    secondaryButtonWeight: Float = 1f,
) {
    IenDoubleBottomCTA(
        modifier = modifier,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
        leftButtonWeight = secondaryButtonWeight,
        rightButtonWeight = primaryButtonWeight,
        leftButton = {
            IenBottomCTAButton(
                text = secondaryText,
                onClick = onSecondaryClick,
                variant = if (isIenSideBarLayout) IenButtonVariant.Line else IenButtonVariant.Weak,
                tone = IenSemanticTone.Neutral,
                state = secondaryState,
                icon = secondaryIcon,
            )
        },
        rightButton = {
            IenBottomCTAButton(
                text = primaryText,
                onClick = onPrimaryClick,
                state = primaryState,
                icon = primaryIcon,
            )
        },
    )
}

/**
 * 커스텀 좌/우 버튼 슬롯을 받아 화면 하단에 듀얼 CTA 버튼 영역을 구성하는 컴포저블입니다.
 *
 * Duo의 오른쪽 바에서는 버튼을 세로로 쌓고, 일반 하단 바에서는 너비 비율을 적용합니다.
 * 한쪽 슬롯이 null이면 남은 버튼 영역이 비율과 관계없이 전체 너비를 차지합니다.
 * 두 슬롯이 모두 null이면 버튼 영역을 표시하지 않습니다.
 *
 * @param leftButton 좌측 버튼 컴포저블 슬롯. null이면 좌측 영역을 생성하지 않습니다.
 * @param rightButton 우측 버튼 컴포저블 슬롯. null이면 우측 영역을 생성하지 않습니다.
 * @param leftButtonWeight 두 버튼이 모두 있을 때 좌측 버튼 영역의 상대 너비 비율
 * @param rightButtonWeight 두 버튼이 모두 있을 때 우측 버튼 영역의 상대 너비 비율
 * @param modifier 적용할 Modifier
 * @param background 하단 바 배경 스타일 ([IenBottomCTABackground])
 * @param hasSafeAreaPadding 세이프 에어리어 패딩 계산 적용 여부
 * @param hasPaddingBottom 하단 여백 추가 여부
 * @param fixed 화면 하단 고정 여부
 * @param takeSpace 레이아웃 공간 차지 여부
 * @param show 가시성 제어 플래그
 * @param showAfterDelay 지연 등장 설정
 * @param hideOnScroll 스크롤 시 숨김 여부
 * @param hideOnScrollDistanceThreshold 스크롤 감지 임계치
 * @param scrollDelta 스크롤 변화량
 * @param topAccessory 추가 상단 컴포저블
 * @param bottomAccessory 추가 하단 컴포저블
 */
@Composable
fun IenDoubleBottomCTA(
    leftButton: (@Composable RowScope.() -> Unit)? = null,
    rightButton: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixed: Boolean = false,
    takeSpace: Boolean = fixed,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
    leftButtonWeight: Float = 1f,
    rightButtonWeight: Float = 1f,
) {
    val hasBothButtons = leftButton != null && rightButton != null
    if (hasBothButtons) {
        require(leftButtonWeight.isFinite() && leftButtonWeight > 0f) {
            "leftButtonWeight는 0보다 큰 유한한 값이어야 합니다."
        }
        require(rightButtonWeight.isFinite() && rightButtonWeight > 0f) {
            "rightButtonWeight는 0보다 큰 유한한 값이어야 합니다."
        }
    }

    IenBottomCTAContainer(
        modifier = modifier,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = fixed,
        fixedAboveKeyboard = false,
        takeSpace = takeSpace,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    ) {
        if (isIenSideBarLayout) {
            Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs)) {
                if (leftButton != null) Row(Modifier.fillMaxWidth(), content = leftButton)
                if (rightButton != null) Row(Modifier.fillMaxWidth(), content = rightButton)
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leftButton != null) {
                    Row(
                        modifier = Modifier.weight(if (hasBothButtons) leftButtonWeight else 1f).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        leftButton()
                    }
                }
                if (rightButton != null) {
                    Row(
                        modifier = Modifier.weight(if (hasBothButtons) rightButtonWeight else 1f).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        rightButton()
                    }
                }
            }
        }
    }
}

/**
 * [IenDoubleBottomCTA] 등 하단 가로 행 형태의 CTA 영역 내부에 배치할 개별 버튼 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param state 버튼 상태 ([IenButtonState])
 * @param icon 버튼 왼쪽에 표시할 아이콘 컴포저블
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 */
@Composable
fun RowScope.IenBottomCTAButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
) {
    IenButtonContainer(
        onClick = onClick,
        modifier = modifier
            .weight(1f)
            .fillMaxWidth()
            .heightIn(min = IenButtonSize.Large.buttonHeight()),
        variant = variant,
        tone = tone,
        state = state,
        shape = ContinuousRoundedRectangle(if (isIenSideBarLayout) IenTheme.radius.full else IenTheme.radius.default),
        contentPadding = if (isIenSideBarLayout) PaddingValues(
            horizontal = IenTheme.spacing.xxs,
            vertical = IenTheme.spacing.xs,
        ) else IenButtonSize.Large.buttonPadding(),
        colors = IenButtonDefault.colors(variant = variant, tone = tone),
        scalePressed = 0.975f,
    ) {
        IenBottomCTAButtonContent(
            text = text,
            loading = state.loading,
            icon = icon,
        )
    }
}

/**
 * [BoxScope] 하단에 고정하며, Duo에서는 오른쪽 시스템 예약 영역에 맞춰 배치하는 단일 CTA입니다.
 * Duo의 시스템 좌표를 적용할 수 있도록 부모 Box는 화면 전체 영역을 사용해야 합니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 클릭 이벤트 콜백
 * @param modifier 적용할 Modifier
 * @param contentPadding 내부 패딩 (미사용 시 null)
 * @param state 버튼 상태 ([IenButtonState])
 * @param icon 버튼 왼쪽에 표시할 아이콘 컴포저블
 * @param variant 버튼 스타일 변형 ([IenButtonVariant])
 * @param tone 버튼 색상 톤 ([IenSemanticTone])
 * @param background 하단 바 배경 스타일 ([IenBottomCTABackground])
 * @param hasSafeAreaPadding 세이프 에어리어 패딩 계산 적용 여부
 * @param hasPaddingBottom 하단 여백 추가 여부
 * @param fixedAboveKeyboard 키보드 작동 시 키보드 상단 밀어올림 고정 여부
 * @param show 가시성 제어 플래그
 * @param showAfterDelay 지연 등장 설정
 * @param hideOnScroll 스크롤 시 숨김 여부
 * @param hideOnScrollDistanceThreshold 스크롤 감지 임계치
 * @param scrollDelta 스크롤 변화량
 * @param topAccessory 추가 상단 컴포저블
 * @param bottomAccessory 추가 하단 컴포저블
 */
@Composable
fun BoxScope.IenFixedBottomCTA(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues? = null,
    state: IenButtonState = IenButtonState(),
    icon: (@Composable () -> Unit)? = null,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    fixedAboveKeyboard: Boolean = false,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
) {
    IenBottomCTA(
        text = text,
        onClick = onClick,
        modifier = modifier.align(Alignment.BottomCenter),
        state = state,
        icon = icon,
        variant = variant,
        tone = tone,
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = true,
        fixedAboveKeyboard = fixedAboveKeyboard,
        takeSpace = false,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
    )
}

/**
 * [BoxScope] 하단에 고정하며, Duo에서는 오른쪽 시스템 예약 영역에 맞춰 배치하는 듀얼 CTA입니다.
 * Duo의 시스템 좌표를 적용할 수 있도록 부모 Box는 화면 전체 영역을 사용해야 합니다.
 *
 * 한쪽 슬롯이 null이면 남은 버튼 영역이 비율과 관계없이 전체 너비를 차지합니다.
 * 두 슬롯이 모두 null이면 버튼 영역을 표시하지 않습니다.
 *
 * @param leftButton 좌측 버튼 컴포저블 슬롯. null이면 좌측 영역을 생성하지 않습니다.
 * @param rightButton 우측 버튼 컴포저블 슬롯. null이면 우측 영역을 생성하지 않습니다.
 * @param leftButtonWeight 두 버튼이 모두 있을 때 좌측 버튼 영역의 상대 너비 비율
 * @param rightButtonWeight 두 버튼이 모두 있을 때 우측 버튼 영역의 상대 너비 비율
 * @param modifier 적용할 Modifier
 * @param background 하단 바 배경 스타일 ([IenBottomCTABackground])
 * @param hasSafeAreaPadding 세이프 에어리어 패딩 계산 적용 여부
 * @param hasPaddingBottom 하단 여백 추가 여부
 * @param show 가시성 제어 플래그
 * @param showAfterDelay 지연 등장 설정
 * @param hideOnScroll 스크롤 시 숨김 여부
 * @param hideOnScrollDistanceThreshold 스크롤 감지 임계치
 * @param scrollDelta 스크롤 변화량
 * @param topAccessory 추가 상단 컴포저블
 * @param bottomAccessory 추가 하단 컴포저블
 */
@Composable
fun BoxScope.IenFixedDoubleBottomCTA(
    leftButton: (@Composable RowScope.() -> Unit)? = null,
    rightButton: (@Composable RowScope.() -> Unit)? = null,
    modifier: Modifier = Modifier,
    background: IenBottomCTABackground = IenBottomCTABackground.Default,
    hasSafeAreaPadding: Boolean = true,
    hasPaddingBottom: Boolean = true,
    show: Boolean = true,
    showAfterDelay: IenBottomCTAShowAfterDelay? = null,
    hideOnScroll: Boolean = false,
    hideOnScrollDistanceThreshold: Float = 1f,
    scrollDelta: Float = 0f,
    topAccessory: (@Composable () -> Unit)? = null,
    bottomAccessory: (@Composable () -> Unit)? = null,
    leftButtonWeight: Float = 1f,
    rightButtonWeight: Float = 1f,
) {
    IenDoubleBottomCTA(
        leftButton = leftButton,
        rightButton = rightButton,
        modifier = modifier.align(Alignment.BottomCenter),
        background = background,
        hasSafeAreaPadding = hasSafeAreaPadding,
        hasPaddingBottom = hasPaddingBottom,
        fixed = true,
        takeSpace = false,
        show = show,
        showAfterDelay = showAfterDelay,
        hideOnScroll = hideOnScroll,
        hideOnScrollDistanceThreshold = hideOnScrollDistanceThreshold,
        scrollDelta = scrollDelta,
        topAccessory = topAccessory,
        bottomAccessory = bottomAccessory,
        leftButtonWeight = leftButtonWeight,
        rightButtonWeight = rightButtonWeight,
    )
}

internal fun ienBottomCTASideBounds(
    width: Int,
    height: Int,
    sideWidth: Int,
    controlsTop: Int,
    controlsBottom: Int,
    centerFromRight: Int,
    keyboardBottom: Int,
): IntRect {
    val barWidth = sideWidth.coerceIn(0, width)
    val left = (width - centerFromRight - barWidth / 2).coerceIn(0, width - barWidth)
    val top = controlsTop.coerceIn(0, height)
    val bottom = (height - maxOf(controlsBottom, keyboardBottom)).coerceIn(top, height)
    return IntRect(left, top, left + barWidth, bottom)
}

@OptIn(ExperimentalCupertinoApi::class)
@Composable
private fun IenBottomCTAContainer(
    modifier: Modifier,
    background: IenBottomCTABackground,
    hasSafeAreaPadding: Boolean,
    hasPaddingBottom: Boolean,
    fixed: Boolean,
    fixedAboveKeyboard: Boolean,
    takeSpace: Boolean,
    show: Boolean,
    showAfterDelay: IenBottomCTAShowAfterDelay?,
    hideOnScroll: Boolean,
    hideOnScrollDistanceThreshold: Float,
    scrollDelta: Float,
    topAccessory: (@Composable () -> Unit)?,
    bottomAccessory: (@Composable () -> Unit)?,
    content: @Composable () -> Unit,
) {
    val inheritedPlacement = LocalIenBarPlacement.current
    val placement = if (LocalIenSideBarEnabled.current) {
        (inheritedPlacement ?: rememberIenBarPlacement().value)
            ?.takeIf { it.sideWidth > IenTheme.spacing.none }
    } else null
    val isSideBar = placement != null
    val hasWideAccessories = isSideBar && (topAccessory != null || bottomAccessory != null)
    var delayedVisible by remember(showAfterDelay) { mutableStateOf(showAfterDelay == null) }
    LaunchedEffect(show, showAfterDelay) {
        if (show && showAfterDelay != null) {
            delayedVisible = false
            delay(showAfterDelay.delayMillis.toLong())
            delayedVisible = true
        } else {
            delayedVisible = show
        }
    }
    val visible = show && delayedVisible && !(hideOnScroll && scrollDelta > hideOnScrollDistanceThreshold)
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
    )
    val scale by animateFloatAsState(
        targetValue = if (visible || showAfterDelay?.animation != IenBottomCTAAnimation.Scale) 1f else 0.96f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    )
    val translation by animateFloatAsState(
        targetValue = if (visible || showAfterDelay?.animation != IenBottomCTAAnimation.Slide) 0f else 18f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMediumLow),
    )
    val navigationBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val keyboardBottom =
        if (fixedAboveKeyboard) WindowInsets.ime.asPaddingValues().calculateBottomPadding()
        else IenTheme.spacing.none
    val defaultBottom = IenTheme.spacing.lg
    val safeBottom = when {
        hasWideAccessories && placement != null && !fixed && inheritedPlacement == null -> placement.controlsBottom
        isSideBar -> IenTheme.spacing.none
        !hasPaddingBottom -> IenTheme.spacing.none
        hasSafeAreaPadding -> if (navigationBottom > defaultBottom) navigationBottom else defaultBottom
        else -> defaultBottom
    }
    val bottomPadding = if (keyboardBottom > safeBottom) keyboardBottom else safeBottom
    val shouldCompose = visible || takeSpace

    if (shouldCompose) {
        CompositionLocalProvider(LocalIenBarPlacement provides placement) {
            Box(
                modifier = modifier
                    .then(if (isSideBar && !fixed && !hasWideAccessories) Modifier.cupertinoScaffoldBottomBar() else Modifier)
                    .then(if (fixed && placement != null) Modifier.layout { measurable, constraints ->
                        if (constraints.hasBoundedWidth && constraints.hasBoundedHeight) {
                            val bounds = ienBottomCTASideBounds(
                                width = constraints.maxWidth,
                                height = constraints.maxHeight,
                                sideWidth = placement.sideWidth.roundToPx(),
                                controlsTop = placement.controlsTop.roundToPx(),
                                controlsBottom = placement.controlsBottom.roundToPx(),
                                centerFromRight = placement.centerFromRight.roundToPx(),
                                keyboardBottom = keyboardBottom.roundToPx(),
                            )
                            val child = measurable.measure(Constraints(
                                maxWidth = if (hasWideAccessories) constraints.maxWidth else bounds.width,
                                maxHeight = bounds.height,
                            ))
                            layout(constraints.maxWidth, constraints.maxHeight) {
                                val left = if (hasWideAccessories) 0 else bounds.left + (bounds.width - child.width) / 2
                                child.place(left, bounds.bottom - child.height)
                            }
                        } else {
                            val child = measurable.measure(constraints)
                            layout(child.width, child.height) { child.place(0, 0) }
                        }
                    } else Modifier)
                    .then(if (placement != null && !hasWideAccessories) Modifier.width(placement.sideWidth) else Modifier.fillMaxWidth())
                    .then(if (hasWideAccessories && background == IenBottomCTABackground.Default) {
                        Modifier.background(IenTheme.colors.background)
                    } else Modifier)
                    .graphicsLayer {
                        this.alpha = alpha
                        scaleX = scale
                        scaleY = scale
                        if (isSideBar) translationX = translation else translationY = translation
                    },
                contentAlignment = if (isSideBar) Alignment.TopCenter else Alignment.TopStart,
            ) {
                Column(
                    modifier = Modifier
                        .then(if (isSideBar && !hasWideAccessories) Modifier.width(48.dp + IenTheme.spacing.xxs * 2) else Modifier.fillMaxWidth())
                        .padding(
                            start = if (isSideBar && !hasWideAccessories) IenTheme.spacing.xxs else IenTheme.spacing.md,
                            top = if (isSideBar) IenTheme.spacing.xxs else IenTheme.spacing.md,
                            end = if (isSideBar && !hasWideAccessories) IenTheme.spacing.xxs else IenTheme.spacing.md,
                            bottom = if (isSideBar) safeBottom + IenTheme.spacing.xxs else bottomPadding,
                        ),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
                ) {
                    topAccessory?.invoke()
                    if (hasWideAccessories && placement != null) {
                        Box(Modifier.fillMaxWidth(), contentAlignment = AbsoluteAlignment.CenterRight) {
                            Box(Modifier
                                .width(48.dp)
                                .absoluteOffset(x = IenTheme.spacing.md + 24.dp - placement.centerFromRight),
                            ) {
                                content()
                            }
                        }
                    } else {
                        content()
                    }
                    bottomAccessory?.invoke()
                }
            }
        }
    }
}
