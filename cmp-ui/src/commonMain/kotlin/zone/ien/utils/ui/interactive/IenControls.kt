package zone.ien.utils.ui.interactive

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.next
import zone.ien.utils.cmp_ui.generated.resources.stepper_step_list
import zone.ien.utils.cmp_ui.generated.resources.tab_list
import zone.ien.utils.cmp_ui.generated.resources.tab_update_indicator_desc
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.icon.remix.RemixIcons
import zone.ien.utils.icon.remix.line.ArrowRightS
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText
import kotlin.time.Duration.Companion.milliseconds

/**
 * 범위를 가지는 슬라이더 컴포저블.
 *
 * 사용자는 드래그나 탭 동작을 통해 특정 범위 내의 값을 선택할 수 있습니다.
 *
 * @param value 슬라이더의 현재 값.
 * @param onValueChange 값이 변경될 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param valueRange 슬라이더의 최소값 및 최대값 범위. 기본값은 0f..1f.
 * @param steps 슬라이더 트랙 상의 단계 수. 0일 경우 연속적으로 선택 가능합니다.
 * @param enabled 활성화 여부. false일 경우 상호작용할 수 없습니다.
 * @param label 슬라이더 좌측에 표시될 이름 또는 라벨 텍스트.
 * @param valueLabel 슬라이더 우측에 표시될 현재 값 관련 텍스트.
 */
@Composable
fun IenSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    label: String? = null,
    valueLabel: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) {
            IenText(label, modifier = Modifier.weight(0.8f), style = IenTheme.typography.body2)
        }
        Slider(
            value = value.coerceIn(valueRange.start, valueRange.endInclusive),
            onValueChange = onValueChange,
            modifier = Modifier.weight(1.4f),
            valueRange = valueRange,
            steps = steps,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = IenTheme.colors.brand,
                activeTrackColor = IenTheme.colors.brand,
                inactiveTrackColor = IenTheme.colors.brandWeak,
            ),
        )
        if (valueLabel != null) {
            IenText(valueLabel, style = IenTheme.typography.label2, color = IenTheme.colors.textSecondary)
        }
    }
}

/**
 * [IenStepper]의 값 범위를 정의하는 데이터 클래스.
 *
 * @property min 최솟값. 기본값은 [Int.MIN_VALUE].
 * @property max 최댓값. 기본값은 [Int.MAX_VALUE].
 * @property step 한 번에 변경할 값의 증감 폭. 기본값은 1.
 */
@Immutable
data class IenStepperRange(
    val min: Int = Int.MIN_VALUE,
    val max: Int = Int.MAX_VALUE,
    val step: Int = 1,
)

/**
 * 텍스트와 증감 버튼(-, +)을 이용해 정수 값을 조절하는 컴포저블.
 *
 * @param value 현재 정수 값.
 * @param onValueChange 정수 값이 변경될 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param range 증감할 범위 및 단계 속성 ([IenStepperRange]).
 * @param enabled 활성화 여부. false일 경우 상호작용할 수 없습니다.
 * @param label 컴포저블 좌측에 표시될 라벨 텍스트.
 */
@Composable
fun IenStepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IenStepperRange = IenStepperRange(),
    enabled: Boolean = true,
    label: String? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) {
            IenText(label, modifier = Modifier.weight(1f), style = IenTheme.typography.body2)
        }
        StepperAction(
            text = "-",
            enabled = enabled && value > range.min,
            onClick = { onValueChange((value - range.step).coerceAtLeast(range.min)) },
        )
        IenText(value.toString(), style = IenTheme.typography.label1)
        StepperAction(
            text = "+",
            enabled = enabled && value < range.max,
            onClick = { onValueChange((value + range.step).coerceAtMost(range.max)) },
        )
    }
}

/**
 * 진행 단계를 타임라인 형태로 표시해주는 수직 스텝퍼 레이아웃 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param play 하위 스텝 행들의 등장 애니메이션 재생 여부. 기본값은 true.
 * @param delay 애니메이션 시작 전 지연 시간 (초 단위).
 * @param staggerDelay 각 스텝 행 사이의 애니메이션 시작 간격 시간 (초 단위).
 * @param content 스텝퍼 내부 스텝 행들을 정의할 스코프 기반 콘텐츠.
 */
@Composable
fun IenStepper(
    modifier: Modifier = Modifier,
    play: Boolean = true,
    delay: Float = 0f,
    staggerDelay: Float = 0.1f,
    content: @Composable IenStepperScope.() -> Unit,
) {
    val scope = remember { IenStepperScope() }
    val contentDescription = stringResource(Res.string.stepper_step_list)

    scope.play = play
    scope.delayMillis = (delay * 1000).roundToLong()
    scope.staggerDelayMillis = (staggerDelay * 1000).roundToLong()
    scope.rowIndex = 0

    Column(
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
        },
        verticalArrangement = Arrangement.spacedBy(0.dp),
    ) {
        scope.content()
    }
}

/**
 * [IenStepper] 레이아웃 컴포저블 내부의 자식 요소들을 정의할 때 사용되는 스코프 클래스.
 */
@Stable
class IenStepperScope internal constructor() {
    internal var rowIndex: Int = 0
    internal var play: Boolean = true
    internal var delayMillis: Long = 0L
    internal var staggerDelayMillis: Long = 100L

    /**
     * 스텝퍼의 개별 스텝 행을 생성합니다.
     *
     * @param left 스텝 행의 좌측 영역에 들어갈 컴포저블 (예: 스텝 번호나 아이콘).
     * @param center 스텝 행의 중앙 영역에 들어갈 본문 컴포저블.
     * @param modifier 컴포저블에 적용할 [Modifier].
     * @param right 스텝 행의 우측 영역에 들어갈 컴포저블 (생략 가능).
     * @param hideLine 하위 스텝과의 연결선을 그리지 않을지 여부.
     */
    @Composable
    fun Row(
        left: @Composable () -> Unit,
        center: @Composable ColumnScope.() -> Unit,
        modifier: Modifier = Modifier,
        right: (@Composable () -> Unit)? = null,
        hideLine: Boolean = false,
    ) {
        val index = rowIndex++
        IenStepperRow(
            left = left,
            center = center,
            modifier = modifier,
            right = right,
            hideLine = hideLine,
            play = play,
            delayMillis = delayMillis + (index * staggerDelayMillis),
        )
    }
}

/**
 * 타임라인 형태로 시각화되는 스텝퍼의 개별 행 컴포저블.
 *
 * @param left 스텝 행의 좌측 영역에 들어갈 컴포저블.
 * @param center 스텝 행의 중앙 영역에 들어갈 본문 컴포저블.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param right 스텝 행의 우측 영역에 들어갈 컴포저블.
 * @param hideLine 하위 스텝과의 연결선을 감출지 여부.
 * @param play 등장 애니메이션 재생 여부.
 * @param delayMillis 등장 애니메이션의 지연 시간 (밀리초 단위).
 */
@Composable
fun IenStepperRow(
    left: @Composable () -> Unit,
    center: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    right: (@Composable () -> Unit)? = null,
    hideLine: Boolean = false,
    play: Boolean = false,
    delayMillis: Long = 0L,
) {
    var visible by remember { mutableStateOf(!play) }

    LaunchedEffect(play) {
        if (play && !visible) {
            delay(delayMillis.coerceAtLeast(0L).milliseconds)
            visible = true
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            animationSpec = tween(durationMillis = IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
        ) + slideInVertically(
            initialOffsetY = { it / 5 },
            animationSpec = tween(durationMillis = IenTheme.motion.normalMillis, easing = IenTheme.motion.standardEasing),
        ),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.width(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier.size(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    left()
                }
                if (!hideLine) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .width(IenTheme.stroke.thick)
                            .height(28.dp)
                            .clip(RoundedCornerShape(IenTheme.radius.full))
                            .graphicsLayer { alpha = 0.85f }
                            .background(IenTheme.colors.border),
                    )
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 2.dp, bottom = IenTheme.spacing.md),
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
            ) {
                center()
            }

            if (right != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    right()
                }
            }
        }
    }
}

/**
 * [IenStepperTexts]의 타이포그래피 스타일 형태를 정의하는 열거형 클래스.
 */
enum class IenStepperTextsType {
    /** 굵은 작은 라벨 및 보통 본문 스타일 */
    A,
    /** 큰 제목 및 보통 본문 스타일 */
    B,
    /** 보통 크기 라벨 및 캡션 스타일 */
    C,
}

/**
 * 스텝퍼 내부에서 주로 사용되는 스텝의 제목과 설명 텍스트 레이아웃 컴포저블.
 *
 * @param type 텍스트 스타일 종류 ([IenStepperTextsType]).
 * @param title 스텝의 주요 제목 텍스트.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param description 스텝의 상세 설명 텍스트 (생략 가능).
 */
@Composable
fun IenStepperTexts(
    type: IenStepperTextsType,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
) {
    val titleStyle = when (type) {
        IenStepperTextsType.A -> IenTheme.typography.label1
        IenStepperTextsType.B -> IenTheme.typography.title3
        IenStepperTextsType.C -> IenTheme.typography.label1
    }
    val descriptionStyle = when (type) {
        IenStepperTextsType.A,
        IenStepperTextsType.B -> IenTheme.typography.body2
        IenStepperTextsType.C -> IenTheme.typography.caption
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        IenText(
            text = title,
            style = titleStyle,
            color = IenTheme.colors.textPrimary,
        )
        if (description != null) {
            IenText(
                text = description,
                style = descriptionStyle,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

/**
 * 스텝퍼의 왼쪽 영역에 스텝 순서를 표시하는 원형 숫자 아이콘 컴포저블.
 *
 * @param number 표시할 스텝 번호 (1부터 9까지 지원).
 * @param modifier 컴포저블에 적용할 [Modifier].
 */
@Composable
fun IenStepperNumberIcon(
    number: Int,
    modifier: Modifier = Modifier,
) {
    val safeNumber = number.coerceIn(1, 9)
    IenSurface(
        modifier = modifier.size(28.dp),
        color = IenTheme.colors.brandWeak,
        contentColor = IenTheme.colors.brand,
        shape = RoundedCornerShape(IenTheme.radius.full),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.brand.copy(alpha = 0.18f)),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            IenText(
                text = safeNumber.toString(),
                style = IenTheme.typography.label2,
                color = IenTheme.colors.brand,
            )
        }
    }
}

/**
 * [IenStepperAssetFrame]의 모양을 정의하는 열거형 클래스.
 */
enum class IenStepperAssetFrameShape {
    /** 원형 프레임 */
    CircleMedium,
    /** 모서리가 둥근 사각형 프레임 */
    RoundedMedium,
    /** 24dp 크기의 여백 없는 사각형 프레임 */
    CleanW24,
    /** 32dp 크기의 여백 없는 사각형 프레임 */
    CleanW32,
}

/**
 * [IenStepperAssetFrame]에 사용되는 배경색 및 콘텐츠 색상 정의 데이터 클래스.
 *
 * @property backgroundColor 프레임 배경 색상.
 * @property contentColor 프레임 내부 콘텐츠 색상.
 */
@Immutable
data class IenStepperAssetFrameColors(
    val backgroundColor: Color,
    val contentColor: Color
)

/**
 * [IenStepperAssetFrame]의 기본 설정을 제공하는 객체.
 */
object IenStepperAssetFrameDefaults {
    /**
     * [IenStepperAssetFrameColors]의 기본 인스턴스를 생성하는 팩토리 메서드.
     */
    @Composable
    fun colors(
        backgroundColor: Color = IenTheme.colors.surfaceWeak,
        contentColor: Color = IenTheme.colors.textPrimary
    ): IenStepperAssetFrameColors = IenStepperAssetFrameColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

/**
 * 스텝퍼 좌측 영역에서 이미지나 아이콘 등의 애셋을 감싸는 데 사용하는 프레임 컴포저블.
 *
 * @param shape 프레임의 크기 및 형상 ([IenStepperAssetFrameShape]).
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param colors 프레임의 배경 및 콘텐츠 색상 ([IenStepperAssetFrameColors]).
 * @param content 프레임 내부에 그릴 콘텐츠 컴포저블.
 */
@Composable
fun IenStepperAssetFrame(
    shape: IenStepperAssetFrameShape,
    modifier: Modifier = Modifier,
    colors: IenStepperAssetFrameColors = IenStepperAssetFrameDefaults.colors(),
    content: @Composable () -> Unit,
) {
    val size = when (shape) {
        IenStepperAssetFrameShape.CircleMedium,
        IenStepperAssetFrameShape.RoundedMedium,
        IenStepperAssetFrameShape.CleanW32 -> 32.dp
        IenStepperAssetFrameShape.CleanW24 -> 24.dp
    }
    val resolvedShape: Shape = when (shape) {
        IenStepperAssetFrameShape.CircleMedium -> RoundedCornerShape(IenTheme.radius.full)
        IenStepperAssetFrameShape.RoundedMedium -> RoundedCornerShape(IenTheme.radius.default)
        IenStepperAssetFrameShape.CleanW24,
        IenStepperAssetFrameShape.CleanW32 -> RoundedCornerShape(0.dp)
    }

    IenSurface(
        modifier = modifier.size(size),
        color = colors.backgroundColor,
        contentColor = colors.contentColor,
        shape = resolvedShape,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}

/**
 * 스텝퍼의 오른쪽 영역에 다음 단계가 있음을 안내하는 오른쪽 화살표 아이콘 컴포저블.
 *
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param color 화살표 색상. 기본값은 [IenTheme.colors.textTertiary].
 * @param frameSize 프레임의 전체 크기. 기본값은 24.dp.
 */
@Composable
fun IenStepperRightArrow(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.textTertiary,
    frameSize: Dp = 24.dp,
) {
    val contentDescription = stringResource(Res.string.next)
    Box(
        modifier = modifier
            .size(frameSize)
            .semantics { this.contentDescription = contentDescription },
        contentAlignment = Alignment.Center,
    ) {
        IenIcon(
            imageVector = RemixIcons.Line.ArrowRightS,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = color,
        )
    }
}

/**
 * 스텝퍼의 오른쪽 영역에 조작 버튼을 표시하는 컴포저블.
 *
 * @param text 버튼 텍스트.
 * @param onClick 버튼 클릭 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 버튼 크기 ([IenButtonSize]). 기본값은 [IenButtonSize.Small].
 * @param variant 버튼 스타일 종류 ([IenButtonVariant]). 기본값은 [IenButtonVariant.Weak].
 */
@Composable
fun IenStepperRightButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: IenButtonSize = IenButtonSize.Small,
    variant: IenButtonVariant = IenButtonVariant.Weak,
) {
    IenButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        size = size,
        variant = variant,
    )
}

@Composable
private fun StepperAction(
    text: String,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    IenSurface(
        color = if (enabled) IenTheme.colors.brandWeak else IenTheme.colors.surfaceWeak,
        contentColor = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled,
        shape = RoundedCornerShape(IenTheme.radius.full),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Box(
            modifier = Modifier
                .defaultMinSize(minWidth = IenTheme.state.minimumTouchTarget, minHeight = IenTheme.state.minimumTouchTarget)
                .clickable(enabled = enabled, role = Role.Button, onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            IenText(text, style = IenTheme.typography.title3, color = if (enabled) IenTheme.colors.brand else IenTheme.colors.textDisabled)
        }
    }
}

/**
 * [IenTab] 및 [IenFloatingTabBar]에 들어갈 개별 탭 항목의 속성을 정의하는 데이터 클래스.
 *
 * @property text 탭에 표시될 라벨 텍스트.
 * @property enabled 탭 활성화 여부.
 * @property key 탭을 식별하기 위한 선택적 키 객체.
 * @property redBean 새로운 알림 등이 있을 때 표시하는 우상단 빨간색 점(배지) 노출 여부.
 * @property ariaLabel 접근성 스크린 리더용 설명 텍스트.
 * @property icon 탭에 기본 표시될 아이콘 [ImageVector] (주로 플로팅 탭 바 등에서 사용).
 * @property selectedIcon 탭이 선택되었을 때 표시할 아이콘 [ImageVector] (주로 플로팅 탭 바 등에서 사용).
 */
@Immutable
data class IenTabItem(
    val text: String,
    val enabled: Boolean = true,
    val key: Any? = null,
    val redBean: Boolean = false,
    val ariaLabel: String? = null,
    val icon: ImageVector? = null,
    val selectedIcon: ImageVector? = null,
)

/**
 * [IenTab]의 탭 높이 및 텍스트 스타일을 정의하는 열거형 클래스.
 */
enum class IenTabSize {
    /** 작은 탭 크기 */
    Small,
    /** 큰 탭 크기 */
    Large,
}

private data class IenTabItemBounds(
    val left: Dp,
    val width: Dp,
)

/**
 * 상단 또는 특정 영역에서 뷰를 전환하는 데 사용하는 가로 형태의 탭 표시기 컴포저블.
 *
 * @param items 표시할 탭 항목 리스트 ([IenTabItem]).
 * @param selectedIndex 현재 선택된 탭의 인덱스.
 * @param onSelectedIndexChange 탭 선택이 바뀔 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param size 탭의 높이 및 텍스트 크기 ([IenTabSize]). 기본값은 [IenTabSize.Large].
 * @param fluid 가로 스크롤을 활성화하여 탭 아이템 개수나 텍스트 길이에 맞춰 유동적으로 탭을 배치할지 여부.
 * @param itemGap 탭 아이템 사이의 간격.
 * @param ariaLabel 전체 탭 목록에 대한 접근성 설명 텍스트.
 * @param onChange 탭 인덱스가 변경될 때 키 값과 함께 호출되는 선택적 콜백 함수.
 */
@Composable
fun IenTab(
    items: List<IenTabItem>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    size: IenTabSize = IenTabSize.Large,
    fluid: Boolean = false,
    itemGap: Dp? = null,
    ariaLabel: String? = null,
    onChange: ((index: Int, key: Any?) -> Unit)? = null,
) {
    val tabHeight = when (size) {
        IenTabSize.Small -> 40.dp
        IenTabSize.Large -> 48.dp
    }
    val textStyle = when (size) {
        IenTabSize.Small -> IenTheme.typography.label2
        IenTabSize.Large -> IenTheme.typography.label1
    }
    val minItemWidth = when (size) {
        IenTabSize.Small -> 48.dp
        IenTabSize.Large -> 56.dp
    }
    val gap = itemGap ?: if (fluid) IenTheme.spacing.xl else 0.dp
    val density = LocalDensity.current
    val scrollState = rememberScrollState()
    var itemBounds by remember(items) { mutableStateOf<Map<Int, IenTabItemBounds>>(emptyMap()) }
    var viewportWidthPx by remember { mutableStateOf(0) }
    val selectedBounds = itemBounds[selectedIndex]
    val indicatorWidthTarget = selectedBounds?.width?.let { (it - 24.dp).coerceAtLeast(20.dp) } ?: 20.dp
    val indicatorOffsetTarget = selectedBounds?.let { it.left + (it.width - indicatorWidthTarget) / 2 } ?: 0.dp
    val indicatorOffset by animateDpAsState(
        targetValue = indicatorOffsetTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "ienTabIndicatorOffset",
    )
    val indicatorWidth by animateDpAsState(
        targetValue = indicatorWidthTarget,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow,
        ),
        label = "ienTabIndicatorWidth",
    )

    LaunchedEffect(fluid, selectedIndex, selectedBounds, viewportWidthPx, scrollState.maxValue) {
        if (!fluid) return@LaunchedEffect
        val bounds = selectedBounds ?: return@LaunchedEffect
        if (viewportWidthPx <= 0 || scrollState.maxValue <= 0) return@LaunchedEffect

        val itemCenterPx = with(density) { bounds.left.toPx() + bounds.width.toPx() / 2f }
        val targetScroll = (itemCenterPx - viewportWidthPx / 2f)
            .roundToInt()
            .coerceIn(0, scrollState.maxValue)

        scrollState.animateScrollTo(targetScroll)
    }

    val contentDescription = stringResource(Res.string.tab_list)
    Box(
        modifier = modifier
            .onSizeChanged { viewportWidthPx = it.width }
            .then(if (fluid) Modifier.horizontalScroll(scrollState) else Modifier.fillMaxWidth())
            .height(tabHeight)
            .selectableGroup()
            .semantics {
                this.contentDescription = ariaLabel ?: contentDescription
            },
    ) {
        if (selectedBounds != null && indicatorWidth > 0.dp) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = indicatorOffset)
                    .size(width = indicatorWidth, height = 2.dp)
                    .clip(RoundedCornerShape(IenTheme.radius.full))
                    .background(IenTheme.colors.textPrimary),
            )
        }
        Row(
            modifier = Modifier
                .then(if (fluid) Modifier else Modifier.fillMaxWidth())
                .height(tabHeight),
            horizontalArrangement = Arrangement.spacedBy(gap),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()
                val itemScale by animateFloatAsState(
                    targetValue = if (pressed && item.enabled) 0.94f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh,
                    ),
                    label = "ienTabItemPressScale",
                )
                val textColor by animateColorAsState(
                    targetValue = when {
                        !item.enabled -> IenTheme.colors.textDisabled
                        selected -> IenTheme.colors.textPrimary
                        else -> IenTheme.colors.textSecondary
                    },
                    animationSpec = tween(durationMillis = IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
                    label = "ienTabTextColor",
                )
                val contentDescription = stringResource(Res.string.tab_update_indicator_desc, item.text)
                Column(
                    modifier = Modifier
                        .then(if (fluid) Modifier.widthIn(min = minItemWidth) else Modifier.weight(1f))
                        .height(tabHeight)
                        .onGloballyPositioned { coordinates ->
                            val bounds = IenTabItemBounds(
                                left = with(density) { coordinates.positionInParent().x.toDp() },
                                width = with(density) { coordinates.size.width.toDp() },
                            )
                            if (itemBounds[index] != bounds) {
                                itemBounds = itemBounds + (index to bounds)
                            }
                        }
                        .clickable(
                            enabled = item.enabled,
                            role = Role.Tab,
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            if (!selected) {
                                onSelectedIndexChange(index)
                                onChange?.invoke(index, item.key)
                            }
                        }
                        .semantics {
                            this.selected = selected
                            this.contentDescription = item.ariaLabel ?: if (item.redBean) contentDescription else item.text
                        }
                        .padding(horizontal = if (fluid) IenTheme.spacing.xs else 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = itemScale
                                scaleY = itemScale
                            },
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            IenText(
                                text = item.text,
                                style = textStyle,
                                color = textColor,
                                textAlign = TextAlign.Center,
                            )
                            if (item.redBean) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .align(Alignment.TopEnd)
                                        .graphicsLayer {
                                            translationX = 7.dp.toPx()
                                            translationY = (-1).dp.toPx()
                                        }
                                        .clip(CircleShape)
                                        .background(IenTheme.colors.danger),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 화면 하단이나 플로팅 상태로 떠 있는 아이콘 중심의 고급 탭 바 컴포저블.
 *
 * 선택될 때 바운스 애니메이션과 텍스트 노출 효과가 있습니다.
 *
 * @param items 표시할 탭 항목 리스트 ([IenTabItem]).
 * @param selectedIndex 현재 선택된 탭의 인덱스.
 * @param onSelectedIndexChange 탭 선택이 바뀔 때 호출되는 콜백 함수.
 * @param modifier 컴포저블에 적용할 [Modifier].
 * @param ariaLabel 전체 탭 바에 대한 접근성 설명 텍스트.
 * @param onChange 탭 인덱스가 변경될 때 키 값과 함께 호출되는 선택적 콜백 함수.
 */
@Composable
fun IenFloatingTabBar(
    items: List<IenTabItem>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    ariaLabel: String? = null,
    onChange: ((index: Int, key: Any?) -> Unit)? = null,
) {
    val shape = RoundedCornerShape(IenTheme.radius.full)
    val contentDescription = stringResource(Res.string.tab_list)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 18.dp)
            .semantics {
                this.contentDescription = ariaLabel ?: contentDescription
            },
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .height(78.dp)
                .shadow(elevation = 18.dp, shape = shape, clip = false)
                .clip(shape)
                .background(IenTheme.colors.surface)
                .padding(horizontal = 10.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                val itemEnabled = item.enabled
                val icon = if (selected) item.selectedIcon ?: item.icon else item.icon
                val interactionSource = remember { MutableInteractionSource() }
                val pressed by interactionSource.collectIsPressedAsState()
                val selectedBounce = remember { Animatable(1f) }
                val itemScale by animateFloatAsState(
                    targetValue = if (pressed && itemEnabled) 0.94f else 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh,
                    ),
                    label = "ienFloatingTabItemPressScale",
                )
                val contentColor by animateColorAsState(
                    targetValue = when {
                        !itemEnabled -> IenTheme.colors.textDisabled
                        selected -> IenTheme.colors.textPrimary
                        else -> IenTheme.colors.textSecondary
                    },
                    animationSpec = tween(durationMillis = IenTheme.motion.fastMillis, easing = IenTheme.motion.standardEasing),
                    label = "ienFloatingTabItemColor",
                )
                val itemContentDescription = stringResource(Res.string.tab_update_indicator_desc, item.text)

                LaunchedEffect(selected) {
                    if (!selected) {
                        selectedBounce.snapTo(1f)
                        return@LaunchedEffect
                    }

                    selectedBounce.snapTo(1f)
                    selectedBounce.animateTo(
                        targetValue = 1f,
                        animationSpec = keyframes {
                            durationMillis = 360
                            1f at 0
                            1.16f at 110
                            0.96f at 230
                            1f at 360
                        },
                    )
                }

                Column(
                    modifier = Modifier
                        .then(
                            if (selected) {
                                Modifier.widthIn(min = 112.dp)
                            } else {
                                Modifier.width(54.dp)
                            }
                        )
                        .height(66.dp)
                        .graphicsLayer {
                            scaleX = itemScale * selectedBounce.value
                            scaleY = itemScale * selectedBounce.value
                        }
                        .clickable(
                            enabled = itemEnabled,
                            role = Role.Tab,
                            interactionSource = interactionSource,
                            indication = null,
                        ) {
                            if (!selected) {
                                onSelectedIndexChange(index)
                                onChange?.invoke(index, item.key)
                            }
                        }
                        .semantics {
                            this.selected = selected
                            this.contentDescription = item.ariaLabel ?: if (item.redBean) itemContentDescription else item.text
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (icon != null) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            IenIcon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = contentColor,
                            )
                            if (item.redBean) {
                                Box(
                                    modifier = Modifier
                                        .size(5.dp)
                                        .align(Alignment.TopEnd)
                                        .graphicsLayer {
                                            translationX = 6.dp.toPx()
                                            translationY = (-1).dp.toPx()
                                        }
                                        .clip(CircleShape)
                                        .background(IenTheme.colors.danger),
                                )
                            }
                        }
                        if (selected) {
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }
                    if (selected) {
                        IenText(
                            text = item.text,
                            style = IenTheme.typography.label2,
                            color = contentColor,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}
