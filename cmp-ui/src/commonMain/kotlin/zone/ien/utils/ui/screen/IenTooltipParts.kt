package zone.ien.utils.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Text
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.layout
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.delay
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenSpacing
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.view.resolveIenTooltipColors
import zone.ien.utils.ui.window.ienTooltipShadowPopupProperties
import zone.ien.utils.ui.window.ienTooltipPopupProperties
import zone.ien.utils.ui.window.rememberIenTooltipPopupKey

/**
 * 툴팁(Tooltip)의 팝업 위치를 계산하여 제공하는 [PopupPositionProvider] 구현 클래스입니다.
 *
 * @property placement 툴팁 배치 위치 ([IenTooltipPlacement])
 * @property offset 툴팁의 오프셋 간격
 * @property density 화면 밀도 디바이스 설정 ([Density])
 * @property onArrowRatioCalculated 화살표 비율이 계산될 때 호출될 콜백
 */
class IenTooltipPositionProvider(
    private val placement: IenTooltipPlacement,
    private val offset: Dp,
    private val density: Density,
    private val onArrowRatioCalculated: (Float) -> Unit
) : PopupPositionProvider {
    private var shadowPadding: Dp = IenSpacing().none
    private var safeMargin: Dp = IenSpacing().md

    internal constructor(
        placement: IenTooltipPlacement,
        offset: Dp,
        density: Density,
        shadowPadding: Dp,
        safeMargin: Dp,
        onArrowRatioCalculated: (Float) -> Unit,
    ) : this(placement, offset, density, onArrowRatioCalculated) {
        this.shadowPadding = shadowPadding
        this.safeMargin = safeMargin
    }

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val shadowPaddingPx = with(density) { shadowPadding.roundToPx() }
        val tooltipSize = IntSize(
            width = (popupContentSize.width - shadowPaddingPx * 2).coerceAtLeast(0),
            height = (popupContentSize.height - shadowPaddingPx * 2).coerceAtLeast(0),
        )
        val preferredX = anchorBounds.left + (anchorBounds.width - tooltipSize.width) / 2
        val offsetPx = with(density) { offset.toPx().toInt() }
        val safeMarginPx = with(density) { safeMargin.toPx() }

        val minX = safeMarginPx.toInt()
        val maxX = (windowSize.width - tooltipSize.width - safeMarginPx)
            .toInt()
            .coerceAtLeast(minX)
        val x = when (placement) {
            IenTooltipPlacement.Top,
            IenTooltipPlacement.Bottom -> preferredX.coerceIn(minX, maxX)
            IenTooltipPlacement.Left -> {
                (anchorBounds.left - tooltipSize.width - offsetPx)
                    .coerceIn(minX, maxX)
            }
            IenTooltipPlacement.Right -> {
                (anchorBounds.right + offsetPx)
                    .coerceIn(minX, maxX)
            }
        }

        val y = when (placement) {
            IenTooltipPlacement.Top -> {
                anchorBounds.top - tooltipSize.height - offsetPx
            }
            IenTooltipPlacement.Bottom -> {
                anchorBounds.bottom + offsetPx
            }
            IenTooltipPlacement.Left,
            IenTooltipPlacement.Right -> {
                val centeredY = anchorBounds.top + (anchorBounds.height - tooltipSize.height) / 2
                val minY = safeMarginPx.toInt()
                val maxY = (windowSize.height - tooltipSize.height - safeMarginPx)
                    .toInt()
                    .coerceAtLeast(minY)
                centeredY.coerceIn(minY, maxY)
            }
        }

        val anchorCenterX = anchorBounds.left + anchorBounds.width / 2f
        val anchorCenterY = anchorBounds.top + anchorBounds.height / 2f
        val arrowRatio = when (placement) {
            IenTooltipPlacement.Top,
            IenTooltipPlacement.Bottom -> {
                if (tooltipSize.width > 0) {
                    ((anchorCenterX - x) / tooltipSize.width).coerceIn(0.12f, 0.88f)
                } else {
                    0.5f
                }
            }
            IenTooltipPlacement.Left,
            IenTooltipPlacement.Right -> {
                if (tooltipSize.height > 0) {
                    ((anchorCenterY - y) / tooltipSize.height).coerceIn(0.12f, 0.88f)
                } else {
                    0.5f
                }
            }
        }

        onArrowRatioCalculated(arrowRatio)

        return IntOffset(x - shadowPaddingPx, y - shadowPaddingPx)
    }
}

internal fun createIenTooltipPopupPositionProvider(
    placement: IenTooltipPlacement,
    offset: Dp,
    density: Density,
    safeMargin: Dp,
    onArrowRatioCalculated: (Float) -> Unit,
): IenTooltipPositionProvider = IenTooltipPositionProvider(
    placement = placement,
    offset = offset,
    density = density,
    shadowPadding = IenSpacing().none,
    safeMargin = safeMargin,
    onArrowRatioCalculated = onArrowRatioCalculated,
)

/**
 * 말풍선 화살표가 포함된 툴팁 배경 모양([Shape])을 정의하는 클래스입니다.
 *
 * @property placement 툴팁 배치 위치 ([IenTooltipPlacement])
 * @property arrowRatio 화살표 위치 비율 (0.0 ~ 1.0)
 * @property arrowSize 화살표 크기
 * @property cornerRadius 툴팁 모서리 반경
 * @property density 화면 밀도 디바이스 설정 ([Density])
 */
class IenTooltipShape(
    private val placement: IenTooltipPlacement,
    private val arrowRatio: Float,
    private val arrowSize: Dp,
    private val cornerRadius: Dp,
    private val density: Density
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val arrowSizePx = with(this.density) { arrowSize.toPx() }
        val radiusPx = with(this.density) { cornerRadius.toPx() }
        val w = size.width
        val h = size.height

        val path = Path().apply {
            val bodyLeft = if (placement == IenTooltipPlacement.Right) arrowSizePx / 2f else 0f
            val bodyTop = if (placement == IenTooltipPlacement.Bottom) arrowSizePx / 2f else 0f
            val bodyRight = if (placement == IenTooltipPlacement.Left) w - arrowSizePx / 2f else w
            val bodyBottom = if (placement == IenTooltipPlacement.Top) h - arrowSizePx / 2f else h

            val rect = Rect(bodyLeft, bodyTop, bodyRight, bodyBottom)
            addRoundRect(RoundRect(rect, CornerRadius(radiusPx)))

            val arrowWidth = arrowSizePx
            val arrowHeight = arrowSizePx / 2f
            val arrowX = w * arrowRatio.coerceIn(0.1f, 0.9f) - (arrowWidth / 2f)

            when (placement) {
                IenTooltipPlacement.Bottom -> {
                    moveTo(arrowX, bodyTop)
                    lineTo(arrowX + arrowWidth / 2f, bodyTop - arrowHeight)
                    lineTo(arrowX + arrowWidth, bodyTop)
                }
                IenTooltipPlacement.Top -> {
                    moveTo(arrowX, bodyBottom)
                    lineTo(arrowX + arrowWidth / 2f, bodyBottom + arrowHeight)
                    lineTo(arrowX + arrowWidth, bodyBottom)
                }
                IenTooltipPlacement.Left -> {
                    val arrowY = h * arrowRatio.coerceIn(0.1f, 0.9f)
                    moveTo(bodyRight, arrowY - arrowWidth / 2f)
                    lineTo(bodyRight + arrowHeight, arrowY)
                    lineTo(bodyRight, arrowY + arrowWidth / 2f)
                }
                IenTooltipPlacement.Right -> {
                    val arrowY = h * arrowRatio.coerceIn(0.1f, 0.9f)
                    moveTo(bodyLeft, arrowY - arrowWidth / 2f)
                    lineTo(bodyLeft - arrowHeight, arrowY)
                    lineTo(bodyLeft, arrowY + arrowWidth / 2f)
                }
            }
            close()
        }
        return Outline.Generic(path)
    }
}

/**
 * 지정된 앵커 컴포저블 주변에 말풍선 형태의 툴팁(Tooltip)을 표시하는 컴포저블입니다.
 *
 * @param text 툴팁에 표시할 메시지 텍스트
 * @param modifier 적용할 Modifier
 * @param tone 툴팁의 색상 톤 ([IenSemanticTone])
 * @param defaultOpen 초기 오픈 여부 (상태 비제어 시 사용)
 * @param open 오픈 상태 제어용 값 (제어 상태일 때 사용)
 * @param onOpenChange 오픈 상태 변경 시 호출될 콜백 함수
 * @param messageAlign 텍스트 정렬 방식 ([IenTooltipMessageAlign])
 * @param placement 툴팁 노출 방향 설정 ([IenTooltipPlacement])
 * @param motionVariant 애니메이션 효과 종류 ([IenTooltipMotionVariant])
 * @param offset 앵커와 툴팁 간의 오프셋 간격
 * @param anchorPositionByRatio 화살표가 앵커 기준 몇 % 위치에 배치될지 지정하는 비율 (0.0 ~ 1.0)
 * @param openOnHover 호버 시 툴팁을 노출할지 여부
 * @param openOnFocus 포커스 시 툴팁을 노출할지 여부
 * @param dismissible 다른 곳 클릭 시 닫기 가능 여부
 * @param autoFlip 화면 영역 초과 시 위치 자동 반전 여부
 * @param strategy 팝업 배치 전략 ([IenTooltipStrategy])
 * @param clipToEnd 툴팁 클리핑 처리 방식 ([IenTooltipClipToEnd])
 * @param width 툴팁 가로 너비 지정 (선택사항)
 * @param fitContentWidth 텍스트 또는 콘텐츠와 내부 패딩에 맞춰 툴팁 너비를 결정할지 여부
 * @param anchor 툴팁이 가리킬 기준이 되는 컴포저블 (토글 함수가 람다 인자로 전달됨)
 */
@Composable
fun IenTooltip(
    text: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    defaultOpen: Boolean = false,
    open: Boolean? = null,
    onOpenChange: ((Boolean) -> Unit)? = null,
    messageAlign: IenTooltipMessageAlign = IenTooltipMessageAlign.Left,
    placement: IenTooltipPlacement = IenTooltipPlacement.Bottom,
    motionVariant: IenTooltipMotionVariant = IenTooltipMotionVariant.Weak,
    offset: Dp? = null,
    anchorPositionByRatio: Float = 0.5f,
    openOnHover: Boolean = false,
    openOnFocus: Boolean = false,
    dismissible: Boolean = false,
    autoFlip: Boolean = false,
    strategy: IenTooltipStrategy = IenTooltipStrategy.Absolute,
    clipToEnd: IenTooltipClipToEnd = IenTooltipClipToEnd.None,
    width: Dp? = null,
    fitContentWidth: Boolean = false,
    anchor: (@Composable BoxScope.(toggle: () -> Unit) -> Unit)? = null,
) {
    IenTooltipImpl(
        contentDescription = text,
        text = text,
        content = null,
        modifier = modifier,
        tone = tone,
        defaultOpen = defaultOpen,
        open = open,
        onOpenChange = onOpenChange,
        messageAlign = messageAlign,
        placement = placement,
        motionVariant = motionVariant,
        offset = offset,
        anchorPositionByRatio = anchorPositionByRatio,
        openOnHover = openOnHover,
        openOnFocus = openOnFocus,
        dismissible = dismissible,
        autoFlip = autoFlip,
        strategy = strategy,
        clipToEnd = clipToEnd,
        width = width,
        fitContentWidth = fitContentWidth,
        anchor = anchor,
    )
}

/**
 * 지정된 앵커 주변에 호출자가 구성한 콘텐츠를 말풍선 형태의 툴팁으로 표시합니다.
 *
 * @param text 툴팁에 표시할 Composable 콘텐츠
 * @param modifier 적용할 Modifier
 * @param tone 툴팁의 색상 톤 ([IenSemanticTone])
 * @param defaultOpen 초기 오픈 여부 (상태 비제어 시 사용)
 * @param open 오픈 상태 제어용 값 (제어 상태일 때 사용)
 * @param onOpenChange 오픈 상태 변경 시 호출될 콜백 함수
 * @param messageAlign 문자열 콘텐츠의 정렬 방식 ([IenTooltipMessageAlign])
 * @param placement 툴팁 노출 방향 설정 ([IenTooltipPlacement])
 * @param motionVariant 애니메이션 효과 종류 ([IenTooltipMotionVariant])
 * @param offset 앵커와 툴팁 간의 오프셋 간격
 * @param anchorPositionByRatio 화살표가 앵커 기준 몇 % 위치에 배치될지 지정하는 비율 (0.0 ~ 1.0)
 * @param openOnHover 호버 시 툴팁을 노출할지 여부
 * @param openOnFocus 포커스 시 툴팁을 노출할지 여부
 * @param dismissible 다른 곳 클릭 시 닫기 가능 여부
 * @param autoFlip 화면 영역 초과 시 위치 자동 반전 여부
 * @param strategy 팝업 배치 전략 ([IenTooltipStrategy])
 * @param clipToEnd 툴팁 클리핑 처리 방식 ([IenTooltipClipToEnd])
 * @param width 툴팁 가로 너비 지정 (선택사항)
 * @param fitContentWidth 콘텐츠와 내부 패딩에 맞춰 툴팁 너비를 결정할지 여부
 * @param anchor 툴팁이 가리킬 기준이 되는 컴포저블 (토글 함수가 람다 인자로 전달됨)
 */
@Composable
fun IenTooltip(
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    defaultOpen: Boolean = false,
    open: Boolean? = null,
    onOpenChange: ((Boolean) -> Unit)? = null,
    messageAlign: IenTooltipMessageAlign = IenTooltipMessageAlign.Left,
    placement: IenTooltipPlacement = IenTooltipPlacement.Bottom,
    motionVariant: IenTooltipMotionVariant = IenTooltipMotionVariant.Weak,
    offset: Dp? = null,
    anchorPositionByRatio: Float = 0.5f,
    openOnHover: Boolean = false,
    openOnFocus: Boolean = false,
    dismissible: Boolean = false,
    autoFlip: Boolean = false,
    strategy: IenTooltipStrategy = IenTooltipStrategy.Absolute,
    clipToEnd: IenTooltipClipToEnd = IenTooltipClipToEnd.None,
    width: Dp? = null,
    fitContentWidth: Boolean = false,
    anchor: (@Composable BoxScope.(toggle: () -> Unit) -> Unit)? = null,
) {
    IenTooltipImpl(
        contentDescription = null,
        text = null,
        content = text,
        modifier = modifier,
        tone = tone,
        defaultOpen = defaultOpen,
        open = open,
        onOpenChange = onOpenChange,
        messageAlign = messageAlign,
        placement = placement,
        motionVariant = motionVariant,
        offset = offset,
        anchorPositionByRatio = anchorPositionByRatio,
        openOnHover = openOnHover,
        openOnFocus = openOnFocus,
        dismissible = dismissible,
        autoFlip = autoFlip,
        strategy = strategy,
        clipToEnd = clipToEnd,
        width = width,
        fitContentWidth = fitContentWidth,
        anchor = anchor,
    )
}

@Composable
private fun IenTooltipImpl(
    contentDescription: String?,
    text: String?,
    content: (@Composable () -> Unit)?,
    modifier: Modifier,
    tone: IenSemanticTone,
    defaultOpen: Boolean,
    open: Boolean?,
    onOpenChange: ((Boolean) -> Unit)?,
    messageAlign: IenTooltipMessageAlign,
    placement: IenTooltipPlacement,
    motionVariant: IenTooltipMotionVariant,
    offset: Dp?,
    anchorPositionByRatio: Float,
    openOnHover: Boolean,
    openOnFocus: Boolean,
    dismissible: Boolean,
    autoFlip: Boolean,
    strategy: IenTooltipStrategy,
    clipToEnd: IenTooltipClipToEnd,
    width: Dp?,
    fitContentWidth: Boolean,
    anchor: (@Composable BoxScope.(toggle: () -> Unit) -> Unit)?,
) {
    var internalOpen by remember { mutableStateOf(defaultOpen) }
    val isOpen = open ?: internalOpen
    val resolvedPlacement = if (autoFlip && placement == IenTooltipPlacement.Top) {
        IenTooltipPlacement.Bottom
    } else {
        placement
    }
    val resolvedOffset = offset ?: IenTheme.spacing.xs
    val motionScale by animateFloatAsState(
        targetValue = if (isOpen) 1f else motionVariant.hiddenScale(),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = motionVariant.stiffness(),
        ),
    )

    fun updateOpen(next: Boolean) {
        if (open == null) {
            internalOpen = next
        }
        onOpenChange?.invoke(next)
    }

    val toggle = { updateOpen(!isOpen) }

    var keepInComposition by remember { mutableStateOf(isOpen) }
    LaunchedEffect(isOpen) {
        if (isOpen) {
            keepInComposition = true
        } else if (keepInComposition) {
            delay(150L)
            keepInComposition = false
        }
    }

    var tooltipBodySize by remember { mutableStateOf(IntSize.Zero) }
    val shadowPopupProperties = ienTooltipShadowPopupProperties()

    var dynamicArrowRatio by remember { mutableStateOf(anchorPositionByRatio) }
    LaunchedEffect(anchorPositionByRatio) {
        dynamicArrowRatio = anchorPositionByRatio
    }

    val density = LocalDensity.current

    Box(
        modifier = modifier
            .pointerInput(isOpen) {
                detectTapGestures(
                    onTap = {
                        toggle()
                    },
                    onLongPress = {
                        updateOpen(true)
                    }
                )
            }
            .onFocusChanged { focusState ->
                if (openOnFocus) {
                    updateOpen(focusState.isFocused)
                }
            }
            .then(if (openOnFocus) Modifier.focusable() else Modifier)
            .semantics {
                contentDescription?.let { description ->
                    this.contentDescription = description
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        anchor?.invoke(this, toggle)
        if (anchor == null) {
            Text("?", style = IenTheme.typography.label1, color = IenTheme.colors.brand)
        }

        if (keepInComposition && !LocalInspectionMode.current) {
            val popupKey = rememberIenTooltipPopupKey()
            val safeMargin = IenTheme.spacing.md
            key(popupKey) {
                if (shadowPopupProperties != null) {
                    Popup(
                        popupPositionProvider = remember(resolvedPlacement, resolvedOffset, density, safeMargin) {
                            IenTooltipPositionProvider(
                                placement = resolvedPlacement,
                                offset = resolvedOffset,
                                density = density,
                                shadowPadding = IenTooltipShadowPadding,
                                safeMargin = safeMargin,
                                onArrowRatioCalculated = {},
                            )
                        },
                        properties = shadowPopupProperties,
                    ) {
                        IenTooltipPopup(
                            visible = isOpen,
                            text = null,
                            content = null,
                            tone = tone,
                            messageAlign = messageAlign,
                            anchorPositionByRatio = dynamicArrowRatio,
                            clipToEnd = clipToEnd,
                            placement = resolvedPlacement,
                            motionVariant = motionVariant,
                            scale = motionScale,
                            width = width,
                            fitContentWidth = fitContentWidth,
                            shadowOnly = true,
                            bodySize = tooltipBodySize,
                        )
                    }
                }
                Popup(
                    popupPositionProvider = remember(resolvedPlacement, resolvedOffset, density, safeMargin) {
                        createIenTooltipPopupPositionProvider(
                            placement = resolvedPlacement,
                            offset = resolvedOffset,
                            density = density,
                            safeMargin = safeMargin,
                        ) { ratio ->
                            dynamicArrowRatio = ratio
                        }
                    },
                    onDismissRequest = { updateOpen(false) },
                    properties = ienTooltipPopupProperties()
                ) {
                    IenTooltipPopup(
                        visible = isOpen,
                        text = text,
                        content = content,
                        tone = tone,
                        messageAlign = messageAlign,
                        anchorPositionByRatio = dynamicArrowRatio,
                        clipToEnd = clipToEnd,
                        placement = resolvedPlacement,
                        motionVariant = motionVariant,
                        scale = motionScale,
                        width = width,
                        fitContentWidth = fitContentWidth,
                        drawShadow = shadowPopupProperties == null,
                        onBodySizeChanged = { tooltipBodySize = it },
                    )
                }
            }
        }
    }
}



/**
 * 툴팁 메시지 텍스트의 정렬 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenTooltipMessageAlign {
    Left,
    Center,
    Right,
}

/**
 * 앵커 컴포저블을 기준으로 툴팁이 노출될 방향(위, 아래, 왼쪽, 오른쪽)을 정의하는 열거형 클래스입니다.
 */
enum class IenTooltipPlacement {
    Top,
    Bottom,
    Left,
    Right,
}

/**
 * 툴팁이 노출/소멸될 때의 애니메이션 모션 스타일 강도를 정의하는 열거형 클래스입니다.
 */
enum class IenTooltipMotionVariant {
    Weak,
    Strong,
}

/**
 * 툴팁 팝업의 배치 위치 전략을 정의하는 열거형 클래스입니다.
 */
enum class IenTooltipStrategy {
    Absolute,
    Fixed,
}

/**
 * 툴팁 팝업이 화면 가장자리에 걸칠 때 끝부분을 정렬/클리핑하는 방식을 정의하는 열거형 클래스입니다.
 */
enum class IenTooltipClipToEnd {
    None,
    Left,
    Right,
}

private val IenTooltipShadowPadding = 48.dp

@Composable
private fun IenTooltipPopup(
    visible: Boolean,
    text: String?,
    content: (@Composable () -> Unit)?,
    tone: IenSemanticTone,
    messageAlign: IenTooltipMessageAlign,
    anchorPositionByRatio: Float,
    clipToEnd: IenTooltipClipToEnd,
    placement: IenTooltipPlacement,
    motionVariant: IenTooltipMotionVariant,
    scale: Float,
    width: Dp?,
    fitContentWidth: Boolean,
    shadowOnly: Boolean = false,
    bodySize: IntSize = IntSize.Zero,
    drawShadow: Boolean = true,
    onBodySizeChanged: (IntSize) -> Unit = {},
) {
    val tooltipColors = resolveIenTooltipColors(tone, IenTheme.colors)
    val arrowRatio = anchorPositionByRatio.coerceIn(0.05f, 0.95f)
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = when (motionVariant) {
                IenTooltipMotionVariant.Weak -> IenTheme.motion.fastMillis
                IenTooltipMotionVariant.Strong -> IenTheme.motion.normalMillis
            },
            easing = IenTheme.motion.standardEasing,
        ),
    )

    val density = LocalDensity.current
    val tooltipShape = remember(placement, arrowRatio, density) {
        IenTooltipShape(
            placement = placement,
            arrowRatio = arrowRatio,
            arrowSize = 12.dp,
            cornerRadius = 12.dp,
            density = density
        )
    }

    AnimatedVisibility(
        modifier = if (shadowOnly) Modifier else Modifier.layout { measurable, constraints ->
            val padding = IenTooltipShadowPadding.roundToPx()
            val placeable = measurable.measure(
                constraints.offset(horizontal = padding * 2, vertical = padding * 2),
            )
            layout(
                width = (placeable.width - padding * 2).coerceAtLeast(0),
                height = (placeable.height - padding * 2).coerceAtLeast(0),
            ) {
                placeable.place(-padding, -padding)
            }
        },
        visible = visible,
        enter = fadeIn(tween(IenTheme.motion.fastMillis)),
        exit = fadeOut(tween(IenTheme.motion.fastMillis)),
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                    translationX = when (placement) {
                        IenTooltipPlacement.Left -> if (visible) 0f else 4f
                        IenTooltipPlacement.Right -> if (visible) 0f else -4f
                        IenTooltipPlacement.Top,
                        IenTooltipPlacement.Bottom -> 0f
                    }
                    translationY = when (placement) {
                        IenTooltipPlacement.Top -> if (visible) 0f else 4f
                        IenTooltipPlacement.Bottom -> if (visible) 0f else -4f
                        IenTooltipPlacement.Left,
                        IenTooltipPlacement.Right -> 0f
                    }
                }
                .padding(IenTooltipShadowPadding)
                .then(
                    if (shadowOnly) {
                        with(density) { Modifier.size(bodySize.width.toDp(), bodySize.height.toDp()) }
                    } else if (width != null) Modifier.width(width) else Modifier.widthIn(max = 280.dp),
                ),
            contentAlignment = Alignment.Center
        ) {
            val shadowModifier = if (drawShadow) {
                Modifier.shadow(
                    elevation = 16.dp,
                    shape = tooltipShape,
                    clip = false,
                    ambientColor = Color(0x80001D3A),
                    spotColor = Color(0x80001D3A),
                )
            } else Modifier
            if (shadowOnly) {
                Box(Modifier.matchParentSize().then(shadowModifier))
            } else IenSurface(
                modifier = shadowModifier.onSizeChanged(onBodySizeChanged),
                color = tooltipColors.container,
                contentColor = tooltipColors.content,
                shape = tooltipShape,
            ) {
                val topPadding = if (placement == IenTooltipPlacement.Bottom) 16.dp else 10.dp
                val bottomPadding = if (placement == IenTooltipPlacement.Top) 16.dp else 10.dp
                val startPadding = if (placement == IenTooltipPlacement.Left) 10.dp else 16.dp
                val endPadding = if (placement == IenTooltipPlacement.Right) 10.dp else 16.dp
                val contentWidthModifier = if (fitContentWidth && width == null) {
                    Modifier
                } else {
                    Modifier.fillMaxWidth()
                }

                val contentModifier = contentWidthModifier.padding(
                    start = startPadding,
                    end = endPadding,
                    top = topPadding,
                    bottom = bottomPadding,
                )
                if (text != null) {
                    Text(
                        text = text,
                        modifier = contentModifier,
                        style = IenTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
                        color = tooltipColors.content,
                        textAlign = when (messageAlign) {
                            IenTooltipMessageAlign.Left -> TextAlign.Start
                            IenTooltipMessageAlign.Center -> TextAlign.Center
                            IenTooltipMessageAlign.Right -> TextAlign.End
                        },
                    )
                } else {
                    Box(
                        modifier = contentModifier,
                        contentAlignment = Alignment.Center,
                    ) {
                        content?.invoke()
                    }
                }
            }
        }
    }
}

@Composable
private fun IenTooltipArrowRow(
    color: Color,
    size: Dp,
    ratio: Float,
    placement: IenTooltipPlacement,
    clipToEnd: IenTooltipClipToEnd,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(ratio.coerceIn(0.01f, 0.99f)))
        IenTooltipArrow(color = color, size = size, placement = placement, clipToEnd = clipToEnd)
        Spacer(modifier = Modifier.weight((1f - ratio).coerceIn(0.01f, 0.99f)))
    }
}

@Composable
private fun IenTooltipArrow(
    color: Color,
    size: Dp,
    placement: IenTooltipPlacement,
    clipToEnd: IenTooltipClipToEnd,
) {
    Canvas(
        modifier = Modifier
            .then(
                if (placement == IenTooltipPlacement.Left || placement == IenTooltipPlacement.Right) {
                    Modifier
                        .width(size / 2)
                        .height(size)
                } else {
                    Modifier
                        .width(size)
                        .height(size / 2)
                }
            ),
    ) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            when (placement) {
                IenTooltipPlacement.Bottom -> {
                    moveTo(if (clipToEnd == IenTooltipClipToEnd.Right) w / 2f else 0f, h)
                    lineTo(w / 2f, 0f)
                    lineTo(if (clipToEnd == IenTooltipClipToEnd.Left) w / 2f else w, h)
                }
                IenTooltipPlacement.Top -> {
                    moveTo(if (clipToEnd == IenTooltipClipToEnd.Right) w / 2f else 0f, 0f)
                    lineTo(w / 2f, h)
                    lineTo(if (clipToEnd == IenTooltipClipToEnd.Left) w / 2f else w, 0f)
                }
                IenTooltipPlacement.Left -> {
                    moveTo(0f, 0f)
                    lineTo(w, h / 2f)
                    lineTo(0f, h)
                }
                IenTooltipPlacement.Right -> {
                    moveTo(w, 0f)
                    lineTo(0f, h / 2f)
                    lineTo(w, h)
                }
            }
            close()
        }
        drawPath(path = path, color = color)
    }
}



private fun IenTooltipMotionVariant.hiddenScale(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> 0.98f
    IenTooltipMotionVariant.Strong -> 0.92f
}

private fun IenTooltipMotionVariant.stiffness(): Float = when (this) {
    IenTooltipMotionVariant.Weak -> Spring.StiffnessMediumLow
    IenTooltipMotionVariant.Strong -> Spring.StiffnessMedium
}
