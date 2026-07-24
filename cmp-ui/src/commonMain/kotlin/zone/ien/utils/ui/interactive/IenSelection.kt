package zone.ien.utils.ui.interactive

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.changedToDownIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.launch
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.fill.Check
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.instantPress
import kotlin.math.roundToInt

/**
 * 활성/비활성 상태를 직관적으로 전환할 수 있는 토글 스위치 컴포저블.
 *
 * @param checked 현재 선택(활성화) 상태 여부.
 * @param onCheckedChange 선택 상태 변경 시 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param enabled 활성화 여부. false일 경우 상호작용이 불가능하며 클릭 시 흔들림 애니메이션 효과가 발생합니다.
 * @param thumbContent 스위치 손잡이(Thumb) 내부에 커스텀하게 표시될 컴포저블.
 * @param interactionSource 스위치 인터랙션 정보를 전달할 [MutableInteractionSource].
 */
@Composable
fun IenSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    val checkedTrackColor = if (enabled) {
        IenTheme.colors.brand
    } else {
        IenTheme.colors.brand.copy(alpha = IenTheme.state.disabledAlpha)
    }
    val uncheckedTrackColor = if (enabled) {
        IenTheme.colors.borderStrong
    } else {
        IenTheme.colors.borderStrong.copy(alpha = IenTheme.state.disabledAlpha)
    }
    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    fun shakeDisabledSwitch() {
        coroutineScope.launch {
            val shakeDistance = with(density) { 6.dp.toPx() }
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 200
                    -shakeDistance at 30
                    shakeDistance at 70
                    -shakeDistance * 0.6f at 110
                    shakeDistance * 0.6f at 150
                    0f at 200
                }
            )
        }
    }

    Switch(
        checked = checked,
        onCheckedChange = {
            if (enabled) {
                onCheckedChange(it)
            } else {
                shakeDisabledSwitch()
            }
        },
        modifier = modifier
            .then(
                if (enabled) {
                    Modifier
                } else {
                    Modifier.pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                if (event.changes.any { it.changedToDownIgnoreConsumed() }) {
                                    shakeDisabledSwitch()
                                }
                                event.changes.forEach { it.consume() }
                            }
                        }
                    }
                }
            )
            .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) },
        enabled = true,
        thumbContent = thumbContent,
        interactionSource = interactionSource,
        colors = SwitchDefaults.colors(
            checkedThumbColor = IenTheme.colors.surface,
            checkedTrackColor = checkedTrackColor,
            checkedBorderColor = checkedTrackColor,
            uncheckedThumbColor = IenTheme.colors.surface,
            uncheckedTrackColor = uncheckedTrackColor,
            uncheckedBorderColor = uncheckedTrackColor,
            disabledCheckedThumbColor = IenTheme.colors.surface,
            disabledCheckedTrackColor = checkedTrackColor,
            disabledUncheckedThumbColor = IenTheme.colors.surface,
            disabledUncheckedTrackColor = uncheckedTrackColor,
        ),
    )
}

/**
 * [IenSegmentedControl]의 크기를 정의하는 열거형 클래스.
 */
enum class IenSegmentedControlSize {
    /** 작은 크기 */
    Small,
    /** 큰 크기 */
    Large,
}

/**
 * [IenSegmentedControl] 내부 아이템들의 너비 배치 방식을 정의하는 열거형 클래스.
 */
enum class IenSegmentedControlAlignment {
    /** 아이템들이 사용 가능한 너비를 동일하게 나눠서 채움 (고정폭) */
    Fixed,
    /** 아이템들이 텍스트 길이에 비례하는 너비를 가지고 가로 스크롤 가능하게 함 (유동폭) */
    Fluid,
}

/**
 * 세그먼티드 컨트롤의 개별 아이템 속성을 정의하는 데이터 클래스.
 *
 * @property value 아이템의 실제 식별 값.
 * @property label 아이템에 표시될 라벨 텍스트.
 * @property enabled 개별 아이템의 활성화 여부.
 * @property size 개별 아이템에 적용할 오버라이드 크기 규격. null인 경우 컨트롤 기본 크기를 따릅니다.
 */
data class IenSegmentedControlItem(
    val value: String,
    val label: String,
    val enabled: Boolean = true,
    val size: IenSegmentedControlSize? = null,
)

private data class IenSegmentedControlItemBounds(
    val left: Dp,
    val width: Dp,
)

/**
 * 여러 옵션 중 하나를 선택할 수 있도록 수평으로 나열된 버튼 형태의 세그먼티드 컨트롤 컴포저블.
 *
 * @param items 표시할 옵션 아이템 목록 ([IenSegmentedControlItem]).
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param value 외부 상태로 제어되는 현재 선택된 값. null인 경우 컴포저블 내부 상태를 사용합니다.
 * @param defaultValue 내부 상태로 제어될 때 처음에 선택될 기본 값.
 * @param onChange 선택 값이 변경되었을 때 호출되는 콜백 함수.
 * @param size 세그먼티드 컨트롤의 크기 규격 ([IenSegmentedControlSize]). 기본값은 [IenSegmentedControlSize.Small].
 * @param alignment 옵션 아이템들의 가로 너비 정렬 및 스크롤 옵션 ([IenSegmentedControlAlignment]). 기본값은 [IenSegmentedControlAlignment.Fixed].
 * @param enabled 활성화 여부. false일 경우 상호작용할 수 없습니다.
 */
@Composable
fun IenSegmentedControl(
    items: List<IenSegmentedControlItem>,
    modifier: Modifier = Modifier,
    value: String? = null,
    defaultValue: String? = null,
    onChange: (String) -> Unit = {},
    size: IenSegmentedControlSize = IenSegmentedControlSize.Small,
    alignment: IenSegmentedControlAlignment = IenSegmentedControlAlignment.Fixed,
    enabled: Boolean = true,
) {
    var localValue by remember(items, defaultValue) {
        mutableStateOf(defaultValue ?: items.firstOrNull { it.enabled }?.value ?: items.firstOrNull()?.value.orEmpty())
    }
    val selectedValue = value ?: localValue
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var pressedValue by remember { mutableStateOf<String?>(null) }
    var itemBounds by remember(items) { mutableStateOf<Map<String, IenSegmentedControlItemBounds>>(emptyMap()) }
    var viewportWidthPx by remember { mutableStateOf(0) }
    val height = size.segmentedControlHeight()
    val itemHeight = height - IenTheme.spacing.xxs * 2
    val selectedBounds = itemBounds[selectedValue]
    val indicatorOffset by animateDpAsState(
        targetValue = selectedBounds?.left ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "ienSegmentedControlIndicatorOffset",
    )
    val indicatorWidth by animateDpAsState(
        targetValue = selectedBounds?.width ?: 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "ienSegmentedControlIndicatorWidth",
    )

    LaunchedEffect(alignment, selectedValue, selectedBounds, viewportWidthPx, scrollState.maxValue) {
        if (alignment != IenSegmentedControlAlignment.Fluid) return@LaunchedEffect
        val bounds = selectedBounds ?: return@LaunchedEffect
        if (viewportWidthPx <= 0 || scrollState.maxValue <= 0) return@LaunchedEffect

        val itemCenterPx = with(density) { bounds.left.toPx() + bounds.width.toPx() / 2f }
        val targetScroll = (itemCenterPx - viewportWidthPx / 2f)
            .roundToInt()
            .coerceIn(0, scrollState.maxValue)

        scrollState.animateScrollTo(targetScroll)
    }

    IenSurface(
        modifier = modifier.then(if (alignment == IenSegmentedControlAlignment.Fixed) Modifier.fillMaxWidth() else Modifier),
        color = segmentedControlContainerColor(),
        shape = ContinuousCapsule(),
    ) {
        Box(
            modifier = Modifier
                .onSizeChanged { viewportWidthPx = it.width }
                .then(if (alignment == IenSegmentedControlAlignment.Fixed) Modifier.fillMaxWidth() else Modifier.horizontalScroll(scrollState))
                .height(height)
                .padding(IenTheme.spacing.xxs)
                .selectableGroup(),
        ) {
            if (selectedBounds != null && indicatorWidth > 0.dp) {
                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .height(itemHeight)
                        .widthIn(min = 0.dp)
                        .size(width = indicatorWidth, height = itemHeight)
                        .background(
                            color = segmentedControlIndicatorColor(),
                            shape = ContinuousCapsule(),
                        ),
                )
            }
            Row(
                modifier = Modifier
                    .then(if (alignment == IenSegmentedControlAlignment.Fixed) Modifier.fillMaxWidth() else Modifier)
                    .height(itemHeight),
                horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                items.forEach { item ->
                    val itemSize = item.size ?: size
                    val itemEnabled = enabled && item.enabled
                    val selected = item.value == selectedValue
                    val itemPressed = pressedValue == item.value
                    val itemScale by animateFloatAsState(
                        targetValue = if (itemPressed && itemEnabled) 0.92f else 1f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessHigh,
                        ),
                        label = "ienSegmentedControlItemPressScale",
                    )
                    val itemAlpha by animateFloatAsState(
                        targetValue = if (itemEnabled) 1f else IenTheme.state.disabledAlpha,
                        animationSpec = tween(durationMillis = 150, easing = IenTheme.motion.standardEasing),
                        label = "ienSegmentedControlItemAlpha",
                    )
                    val textColor by animateColorAsState(
                        targetValue = when {
                            !itemEnabled -> IenTheme.colors.textDisabled
                            selected -> IenTheme.colors.textPrimary
                            else -> IenTheme.colors.textSecondary
                        },
                        animationSpec = tween(durationMillis = 120, easing = IenTheme.motion.standardEasing),
                    )

                    Box(
                        modifier = Modifier
                            .then(
                                if (alignment == IenSegmentedControlAlignment.Fixed) {
                                    Modifier.weight(1f)
                                } else {
                                    Modifier.widthIn(min = itemSize.segmentedControlItemMinWidth())
                                },
                            )
                            .height(itemHeight)
                            .onGloballyPositioned { coordinates ->
                                val bounds = IenSegmentedControlItemBounds(
                                    left = with(density) { coordinates.positionInParent().x.toDp() },
                                    width = with(density) { coordinates.size.width.toDp() },
                                )
                                if (itemBounds[item.value] != bounds) {
                                    itemBounds = itemBounds + (item.value to bounds)
                                }
                            }
                            .instantPress(itemEnabled) { pressed ->
                                pressedValue = if (pressed) item.value else null
                            }
                            .graphicsLayer {
                                alpha = itemAlpha
                                scaleX = itemScale
                                scaleY = itemScale
                            }
                            .selectable(
                                selected = selected,
                                enabled = itemEnabled,
                                role = Role.RadioButton,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                if (!selected) {
                                    if (value == null) {
                                        localValue = item.value
                                    }
                                    onChange(item.value)
                                }
                            }
                            .padding(horizontal = itemSize.segmentedControlItemHorizontalPadding()),
                        contentAlignment = Alignment.Center,
                    ) {
                        IenText(
                            text = item.label,
                            style = itemSize.segmentedControlTextStyle(),
                            color = textColor,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IenSegmentedControlSize.segmentedControlHeight(): Dp {
    return when (this) {
        IenSegmentedControlSize.Small -> 40.dp
        IenSegmentedControlSize.Large -> 48.dp
    }
}

@Composable
private fun IenSegmentedControlSize.segmentedControlItemMinWidth(): Dp {
    return when (this) {
        IenSegmentedControlSize.Small -> 64.dp
        IenSegmentedControlSize.Large -> 80.dp
    }
}

@Composable
private fun IenSegmentedControlSize.segmentedControlItemHorizontalPadding(): Dp {
    return when (this) {
        IenSegmentedControlSize.Small -> IenTheme.spacing.sm
        IenSegmentedControlSize.Large -> IenTheme.spacing.md
    }
}

@Composable
private fun IenSegmentedControlSize.segmentedControlTextStyle(): TextStyle {
    return when (this) {
        IenSegmentedControlSize.Small -> IenTheme.typography.label2
        IenSegmentedControlSize.Large -> IenTheme.typography.label1
    }
}

@Composable
private fun segmentedControlContainerColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFFF2F4F6)
    } else {
        Color(0xFF20252B)
    }
}

@Composable
private fun segmentedControlIndicatorColor(): Color {
    return if (IenTheme.colors.background == Color(0xFFFFFFFF)) {
        Color(0xFFFFFFFF)
    } else {
        Color(0xFF343A42)
    }
}

/**
 * 문자열 리스트를 인자로 받아 쉽게 구성할 수 있는 정량 인덱스 기반의 세그먼티드 컨트롤 컴포저블.
 *
 * @param items 표시할 옵션 라벨 문자열 목록.
 * @param selectedIndex 현재 선택된 옵션의 인덱스.
 * @param onSelectedIndexChange 선택된 인덱스가 변경될 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param enabled 활성화 여부.
 */
@Composable
fun IenSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenSegmentedControl(
        items = items.mapIndexed { index, label ->
            IenSegmentedControlItem(
                value = index.toString(),
                label = label,
            )
        },
        modifier = modifier,
        value = selectedIndex.toString(),
        onChange = { value -> onSelectedIndexChange(value.toInt()) },
        enabled = enabled,
    )
}

@Composable
private fun IenCheckMark(
    color: Color,
    modifier: Modifier = Modifier,
) {
    IenIcon(
        imageVector = RemixIcons.Fill.Check,
        contentDescription = null,
        modifier = modifier,
        tint = color,
    )
}

/**
 * 원형 체크박스와 라벨이 함께 제공되는 체크박스 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param checked 외부 상태로 제어되는 현재 체크 여부. null인 경우 컴포저블 내부 상태를 사용합니다.
 * @param defaultChecked 내부 상태로 제어될 때 처음에 적용될 기본 체크 여부.
 * @param onCheckedChange 체크 여부가 변경될 때 호출되는 콜백 함수.
 * @param enabled 활성화 여부. false일 경우 상호작용이 불가능하며 클릭 시 흔들림 애니메이션 효과가 발생합니다.
 * @param size 체크박스의 크기 규격. 기본값은 24.dp.
 * @param label 체크박스 옆에 표시될 설명 라벨 텍스트.
 */
@Composable
fun IenCircleCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    defaultChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    val alpha = if (enabled) 1f else IenTheme.state.disabledAlpha
    val brandColor = IenTheme.colors.brand
    val borderColor = IenTheme.colors.borderStrong

    var localCheckedState by remember { mutableStateOf(defaultChecked ?: false) }
    val isChecked = checked ?: localCheckedState

    val toggleChecked = {
        val target = !isChecked
        if (checked == null) {
            localCheckedState = target
        }
        onCheckedChange?.invoke(target)
    }

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val handleClick = {
        if (enabled) {
            toggleChecked()
        } else {
            coroutineScope.launch {
                val shakeDistance = with(density) { 6.dp.toPx() }
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 200
                        -shakeDistance at 30
                        shakeDistance at 70
                        -shakeDistance * 0.6f at 110
                        shakeDistance * 0.6f at 150
                        0f at 200
                    }
                )
            }
        }
    }

    val targetBgColor = if (isChecked) brandColor else Color.Transparent
    val targetBorderColor = if (isChecked) brandColor else borderColor
    val targetCheckColor = if (isChecked) Color.White else borderColor

    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 200)
    )
    val borderAnimColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = 200)
    )
    val checkAnimColor by animateColorAsState(
        targetValue = targetCheckColor,
        animationSpec = tween(durationMillis = 200)
    )

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .instantPress(enabled) { isPressed = it }
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox
            ) { handleClick() },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val checkboxBackgroundModifier = if (isChecked) {
            Modifier.background(
                brush = toneGradientBrush(IenSemanticTone.Brand),
                shape = androidx.compose.foundation.shape.CircleShape,
            )
        } else {
            Modifier.background(
                color = backgroundColor,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
                .size(size)
                .then(checkboxBackgroundModifier)
                .border(
                    BorderStroke(1.5.dp, borderAnimColor),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            IenIcon(
                imageVector = RemixIcons.Fill.Check,
                contentDescription = null,
                tint = checkAnimColor,
                modifier = Modifier.size(size * 0.65f)
            )
        }
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

/**
 * 체크 시 원형 점(Dot) 형태로 표시되는 미니멀한 체크박스 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param checked 외부 상태로 제어되는 현재 체크 여부. null인 경우 컴포저블 내부 상태를 사용합니다.
 * @param defaultChecked 내부 상태로 제어될 때 처음에 적용될 기본 체크 여부.
 * @param onCheckedChange 체크 여부가 변경될 때 호출되는 콜백 함수.
 * @param enabled 활성화 여부. false일 경우 상호작용이 불가능하며 클릭 시 흔들림 애니메이션 효과가 발생합니다.
 * @param size 체크 영역(점)의 크기 규격. 기본값은 8.dp.
 * @param label 체크박스 옆에 표시될 설명 라벨 텍스트.
 */
@Composable
fun IenDotCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    defaultChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 8.dp,
    label: String? = null,
) {
    val alpha = if (enabled) 1f else IenTheme.state.disabledAlpha
    val brandColor = IenTheme.colors.brand
    val borderColor = IenTheme.colors.borderStrong

    var localCheckedState by remember { mutableStateOf(defaultChecked ?: false) }
    val isChecked = checked ?: localCheckedState

    val toggleChecked = {
        val target = !isChecked
        if (checked == null) {
            localCheckedState = target
        }
        onCheckedChange?.invoke(target)
    }

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val handleClick = {
        if (enabled) {
            toggleChecked()
        } else {
            coroutineScope.launch {
                val shakeDistance = with(density) { 6.dp.toPx() }
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 200
                        -shakeDistance at 30
                        shakeDistance at 70
                        -shakeDistance * 0.6f at 110
                        shakeDistance * 0.6f at 150
                        0f at 200
                    }
                )
            }
        }
    }

    val targetBgColor = if (isChecked) brandColor else Color.Transparent
    val targetBorderColor = if (isChecked) brandColor else borderColor
    val targetCheckColor = if (isChecked) Color.White else borderColor

    val backgroundColor by animateColorAsState(
        targetValue = targetBgColor,
        animationSpec = tween(durationMillis = 200)
    )
    val borderAnimColor by animateColorAsState(
        targetValue = targetBorderColor,
        animationSpec = tween(durationMillis = 200)
    )
    val checkAnimColor by animateColorAsState(
        targetValue = targetCheckColor,
        animationSpec = tween(durationMillis = 200)
    )

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .instantPress(enabled) { isPressed = it }
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox
            ) { handleClick() },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val dotBackgroundModifier = if (isChecked) {
            Modifier.background(
                brush = toneGradientBrush(IenSemanticTone.Brand),
                shape = androidx.compose.foundation.shape.CircleShape,
            )
        } else {
            Modifier.background(
                color = backgroundColor,
                shape = androidx.compose.foundation.shape.CircleShape,
            )
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
                .size(size)
                .then(dotBackgroundModifier)
                .border(
                    BorderStroke(1.5.dp, borderAnimColor),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
        )
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

/**
 * 테두리나 배경 없이 체크 표시(Check icon) 자체만 노출되는 형태의 라인 체크박스 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param checked 외부 상태로 제어되는 현재 체크 여부. null인 경우 컴포저블 내부 상태를 사용합니다.
 * @param defaultChecked 내부 상태로 제어될 때 처음에 적용될 기본 체크 여부.
 * @param onCheckedChange 체크 여부가 변경될 때 호출되는 콜백 함수.
 * @param enabled 활성화 여부. false일 경우 상호작용이 불가능하며 클릭 시 흔들림 애니메이션 효과가 발생합니다.
 * @param size 체크 아이콘의 크기 규격. 기본값은 24.dp.
 * @param label 체크박스 옆에 표시될 설명 라벨 텍스트.
 */
@Composable
fun IenLineCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean? = null,
    defaultChecked: Boolean? = null,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    val alpha = if (enabled) 1f else IenTheme.state.disabledAlpha
    val activeColor = IenTheme.colors.brand
    val inactiveColor = IenTheme.colors.borderStrong

    var localCheckedState by remember { mutableStateOf(defaultChecked ?: false) }
    val isChecked = checked ?: localCheckedState

    val toggleChecked = {
        val target = !isChecked
        if (checked == null) {
            localCheckedState = target
        }
        onCheckedChange?.invoke(target)
    }

    val targetCheckColor = if (isChecked) activeColor else inactiveColor
    val checkAnimColor by animateColorAsState(
        targetValue = targetCheckColor,
        animationSpec = tween(durationMillis = 200)
    )

    val interactionSource = remember { MutableInteractionSource() }
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.95f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
    )

    val density = LocalDensity.current
    val shakeOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()

    val handleClick = {
        if (enabled) {
            toggleChecked()
        } else {
            coroutineScope.launch {
                val shakeDistance = with(density) { 6.dp.toPx() }
                shakeOffset.animateTo(
                    targetValue = 0f,
                    animationSpec = keyframes {
                        durationMillis = 200
                        -shakeDistance at 30
                        shakeDistance at 70
                        -shakeDistance * 0.6f at 110
                        shakeDistance * 0.6f at 150
                        0f at 200
                    }
                )
            }
        }
    }

    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .instantPress(enabled) { isPressed = it }
            .clickable(
                enabled = true,
                interactionSource = interactionSource,
                indication = null,
                role = Role.Checkbox
            ) { handleClick() },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenIcon(
            imageVector = RemixIcons.Fill.Check,
            contentDescription = null,
            tint = checkAnimColor,
            modifier = Modifier
                .offset { IntOffset(x = shakeOffset.value.roundToInt(), y = 0) }
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .alpha(alpha)
                .size(size)
        )
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}
