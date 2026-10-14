package zone.ien.utils.ui.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousCapsule
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.loading
import zone.ien.utils.cmp_ui.generated.resources.progress_stepper_step
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.icon.tabler.line.Check
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneGradientBrush
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenLoaderPrimitive

/**
 * 진행율을 가로 바 형식으로 시각화하여 표현해 주는 프로그레스 바 컴포저블입니다.
 *
 * @param progress 0.0부터 1.0 사이의 진행율 값
 * @param modifier 적용할 [Modifier]
 * @param size 프로그레스 바의 굵기 레벨 ([IenProgressBarSize])
 * @param color 프로그레스 채우기에 적용할 메인 색상
 * @param animate 수치 변경 시 게이지가 차오르는 애니메이션을 적용할지 여부
 * @param contentDescription 접근성을 위한 스크린 리더용 설명문
 * @param showLabel 프로그레스 바 하단에 백분율(예: 50%)을 텍스트 레이블로 보여줄지 여부
 */
@Composable
fun IenProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    size: IenProgressBarSize = IenProgressBarSize.Normal,
    color: Color = IenTheme.colors.brand,
    animate: Boolean = false,
    contentDescription: String? = null,
    showLabel: Boolean = false,
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    val displayedProgress by animateFloatAsState(
        targetValue = safeProgress,
        animationSpec = tween(
            durationMillis = if (animate) IenTheme.motion.normalMillis else 0,
            easing = IenTheme.motion.standardEasing,
        ),
        label = "ienProgressBar",
    )
    val barHeight = when (size) {
        IenProgressBarSize.Light -> 2.dp
        IenProgressBarSize.Normal -> 4.dp
        IenProgressBarSize.Bold -> 8.dp
    }

    Column(
        modifier = modifier.semantics {
            if (contentDescription != null) this.contentDescription = contentDescription
            progressBarRangeInfo = androidx.compose.ui.semantics.ProgressBarRangeInfo(safeProgress, 0f..1f)
        },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(barHeight)
                .clip(ContinuousCapsule)
                .background(color.copy(alpha = 0.16f)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(displayedProgress)
                    .fillMaxHeight()
                    .clip(ContinuousCapsule)
                    .background(
                        brush = if (color == IenTheme.colors.brand) {
                            toneGradientBrush(IenSemanticTone.Brand)
                        } else {
                            androidx.compose.ui.graphics.SolidColor(color)
                        },
                    ),
            )
        }
        if (showLabel) {
            Text(
                text = "${(safeProgress * 100).toInt()}%",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

/**
 * 프로그레스 바의 수직 높이(굵기)를 지정하기 위한 Enum 클래스입니다.
 */
enum class IenProgressBarSize {
    Light,
    Normal,
    Bold,
}

/**
 * 로더([IenLoader]) 인디케이터 크기 단계를 지정하기 위한 Enum 클래스입니다.
 */
enum class IenLoaderSize { Small, Medium, Large }

/**
 * 인디케이터와 선택적인 안내 텍스트로 콘텐츠 대기 상태를 표시하는 로더 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param size 인디케이터 슬롯에 전달할 크기 ([IenLoaderSize])
 * @param label 인디케이터 밑에 표시할 안내 텍스트. 생략 시 인디케이터만 렌더링됩니다.
 * @param indicator 인디케이터 슬롯. 전달된 크기용 [Modifier]를 표시기에 적용합니다.
 * 기본값은 CircularProgressIndicator를 사용하는 [IenLoaderPrimitive]입니다.
 */
@Composable
fun IenLoader(
    modifier: Modifier = Modifier,
    size: IenLoaderSize = IenLoaderSize.Medium,
    label: String? = null,
    indicator: @Composable (Modifier) -> Unit = { indicatorModifier ->
        IenLoaderPrimitive(modifier = indicatorModifier, color = IenTheme.colors.brand)
    },
) {
    val defaultLabel = stringResource(Res.string.loading)
    Column(
        modifier = modifier.semantics {
            contentDescription = label ?: defaultLabel
            liveRegion = LiveRegionMode.Polite
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        indicator(
            Modifier.size(
                when (size) {
                    IenLoaderSize.Small -> IenTheme.icon.sm
                    IenLoaderSize.Medium -> IenTheme.icon.lg
                    IenLoaderSize.Large -> IenTheme.icon.xl
                },
            ),
        )
        if (label != null) {
            Text(label, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
    }
}

/**
 * 단계별 진행 바([IenProgressStepper]) 내 개별 단계의 상태값을 표현하는 Enum 클래스입니다.
 */
enum class IenStepStatus { Pending, Current, Done, Error }

/**
 * 단계별 진행 바의 디자인 레이아웃 방식을 지정하는 Enum 클래스입니다.
 */
enum class IenProgressStepperVariant {
    Compact,
    Icon,
}

/**
 * 단계별 진행 바 내부 상단 여백 수준을 정의하는 Enum 클래스입니다.
 */
enum class IenProgressStepperPaddingTop {
    Default,
    Wide,
}

/**
 * [IenProgressStepper]에 노출할 단계 데이터를 나타내는 데이터 모델입니다.
 *
 * @property title 단계의 명칭/라벨
 * @property status 단계를 활성화하여 표현할 진행 상태 ([IenStepStatus])
 * @property icon 커스텀 아이콘을 표시하고 싶을 때 정의하는 컴포저블 블록
 */
data class IenProgressStep(
    val title: String? = null,
    val status: IenStepStatus? = null,
    val icon: (@Composable () -> Unit)? = null,
) {
    /**
     * 간편하게 라벨과 상태만 지정하여 단계를 구성하는 보조 생성자입니다.
     *
     * @param label 단계 라벨 텍스트
     * @param status 단계 진행 상태 ([IenStepStatus])
     */
    constructor(label: String, status: IenStepStatus) : this(
        title = label,
        status = status,
        icon = null,
    )
}

/**
 * 여러 과정의 단계를 순차적으로 표시하며 현재 진행 단계를 알려주는 스텝 인디케이터 컴포저블입니다.
 *
 * @param steps 구성할 단계 목록 ([IenProgressStep])
 * @param modifier 적용할 [Modifier]
 * @param variant 스텝 표시 스타일 형태 ([IenProgressStepperVariant])
 * @param paddingTop 상단 여백 단계 수준 ([IenProgressStepperPaddingTop])
 * @param activeStepIndex 현재 진행 중인 스텝의 0-based 인덱스
 * @param checkForFinish 완료된 스텝들을 체크 아이콘으로 변경하여 보여줄지 여부
 */
@Composable
fun IenProgressStepper(
    steps: List<IenProgressStep>,
    modifier: Modifier = Modifier,
    variant: IenProgressStepperVariant = IenProgressStepperVariant.Compact,
    paddingTop: IenProgressStepperPaddingTop = IenProgressStepperPaddingTop.Default,
    activeStepIndex: Int = 0,
    checkForFinish: Boolean = false,
) {
    val safeActiveStepIndex = activeStepIndex.coerceIn(0, (steps.size - 1).coerceAtLeast(0))
    val topPadding = when (paddingTop) {
        IenProgressStepperPaddingTop.Default -> IenTheme.spacing.md
        IenProgressStepperPaddingTop.Wide -> IenTheme.spacing.xl
    }
    val contentDescription = stringResource(Res.string.progress_stepper_step, safeActiveStepIndex + 1, steps.size)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = topPadding)
            .semantics {
                this.contentDescription = contentDescription
            },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.none),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { index, step ->
            val status = step.status ?: when {
                index < safeActiveStepIndex -> IenStepStatus.Done
                index == safeActiveStepIndex -> IenStepStatus.Current
                else -> IenStepStatus.Pending
            }
            val color = when (status) {
                IenStepStatus.Pending -> IenTheme.colors.borderStrong
                IenStepStatus.Current -> IenTheme.colors.brand
                IenStepStatus.Done -> IenTheme.colors.brand
                IenStepStatus.Error -> IenTheme.colors.danger
            }
            val titleColor = when (status) {
                IenStepStatus.Pending -> IenTheme.colors.textTertiary
                IenStepStatus.Current -> IenTheme.colors.textPrimary
                IenStepStatus.Done -> IenTheme.colors.textSecondary
                IenStepStatus.Error -> IenTheme.colors.danger
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(progressStepMarkerSize(variant)),
                    contentAlignment = Alignment.Center,
                ) {
                    if (index > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth(0.5f)
                                .height(IenTheme.stroke.thin)
                                .background(
                                    brush = if (index <= safeActiveStepIndex) {
                                        toneGradientBrush(IenSemanticTone.Brand)
                                    } else {
                                        androidx.compose.ui.graphics.SolidColor(IenTheme.colors.border)
                                    },
                                ),
                        )
                    }
                    if (index < steps.lastIndex) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .fillMaxWidth(0.5f)
                                .height(IenTheme.stroke.thin)
                                .background(
                                    brush = if (index < safeActiveStepIndex) {
                                        toneGradientBrush(IenSemanticTone.Brand)
                                    } else {
                                        androidx.compose.ui.graphics.SolidColor(IenTheme.colors.border)
                                    },
                                ),
                        )
                    }
                    ProgressStepMarker(
                        variant = variant,
                        status = status,
                        color = color,
                        icon = step.icon,
                        showFinishedCheck = variant == IenProgressStepperVariant.Icon &&
                            checkForFinish &&
                            index < safeActiveStepIndex,
                    )
                }
                if (step.title != null) {
                    Spacer(Modifier.height(IenTheme.spacing.xs))
                    Text(
                        text = step.title,
                        style = IenTheme.typography.caption,
                        color = titleColor,
                        maxLines = 2,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                }
            }
        }
    }
}

private fun progressStepMarkerSize(variant: IenProgressStepperVariant): Dp =
    when (variant) {
        IenProgressStepperVariant.Compact -> 10.dp
        IenProgressStepperVariant.Icon -> 28.dp
    }

@Composable
private fun ProgressStepMarker(
    variant: IenProgressStepperVariant,
    status: IenStepStatus,
    color: Color,
    icon: (@Composable () -> Unit)?,
    showFinishedCheck: Boolean,
) {
    val markerSize = progressStepMarkerSize(variant)
    val backgroundColor = when {
        status == IenStepStatus.Pending -> IenTheme.colors.surfaceVariant
        variant == IenProgressStepperVariant.Icon -> color
        else -> color
    }

    Box(
        modifier = Modifier
            .size(markerSize)
            .clip(CircleShape)
            .background(
                brush = when {
                    status == IenStepStatus.Pending -> androidx.compose.ui.graphics.SolidColor(backgroundColor)
                    status == IenStepStatus.Error -> toneGradientBrush(IenSemanticTone.Danger)
                    else -> toneGradientBrush(IenSemanticTone.Brand)
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        when {
            showFinishedCheck -> IenIcon(
                imageVector = SystemIcons.Check,
                contentDescription = null,
                tint = IenTheme.colors.onBrand,
                size = IenTheme.icon.sm,
            )
            variant == IenProgressStepperVariant.Icon && icon != null -> icon()
            variant == IenProgressStepperVariant.Icon && status == IenStepStatus.Current -> Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(IenTheme.colors.onBrand),
            )
        }
    }
}
