package zone.ien.utils.ui.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.icon.tabler.line.Check
import zone.ien.utils.icon.tabler.line.Close
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenSurface
import kotlin.math.roundToInt

/**
 * IEN 스낵바의 기본값을 정의합니다.
 */
object IenSnackbarDefaults {
    val MinWidth: Dp? = null
    val MaxWidth: Dp = 420.dp
    val SwipeDismissThreshold: Dp = 48.dp
    val ExitTravelDistance: Dp = 160.dp
    const val FillMaxWidth: Boolean = false
}

/**
 * Material [SnackbarHostState]에 IEN 스낵바의 추가 시각 정보를 전달하기 위한 [SnackbarVisuals] 구현입니다.
 */
@Immutable
data class IenSnackbarVisuals(
    override val message: String,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
    val tone: IenSemanticTone = IenSemanticTone.Neutral,
    val minWidth: Dp? = IenSnackbarDefaults.MinWidth,
    val maxWidth: Dp = IenSnackbarDefaults.MaxWidth,
    val fillMaxWidth: Boolean = IenSnackbarDefaults.FillMaxWidth,
) : SnackbarVisuals

/**
 * Material [SnackbarHostState]를 사용해 IEN 스타일 스낵바를 표시합니다.
 */
suspend fun SnackbarHostState.showIenSnackbar(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = false,
    duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    minWidth: Dp? = IenSnackbarDefaults.MinWidth,
    maxWidth: Dp = IenSnackbarDefaults.MaxWidth,
    fillMaxWidth: Boolean = IenSnackbarDefaults.FillMaxWidth,
): SnackbarResult {
    return showSnackbar(
        IenSnackbarVisuals(
            message = message,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            duration = duration,
            tone = tone,
            minWidth = minWidth,
            maxWidth = maxWidth,
            fillMaxWidth = fillMaxWidth,
        )
    )
}

/**
 * Material [SnackbarHost]를 IEN 디자인으로 렌더링하는 호스트입니다.
 */
@Composable
fun IenSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    var displayedData by remember { mutableStateOf<SnackbarData?>(null) }
    val currentData = hostState.currentSnackbarData
    val normalMillis = IenTheme.motion.normalMillis
    val fastMillis = IenTheme.motion.fastMillis
    val standardEasing = IenTheme.motion.standardEasing
    val exitTravelDistance = with(LocalDensity.current) { IenSnackbarDefaults.ExitTravelDistance.roundToPx() }

    LaunchedEffect(currentData) {
        if (currentData != null) {
            displayedData = currentData
        } else {
            withFrameMillis { }
            if (hostState.currentSnackbarData == null) {
                displayedData = null
            }
        }
    }

    LaunchedEffect(currentData) {
        val data = currentData ?: return@LaunchedEffect
        val durationMillis = data.visuals.duration.ienSnackbarDurationMillis() ?: return@LaunchedEffect
        delay(durationMillis)
        if (hostState.currentSnackbarData === data) {
            data.dismiss()
        }
    }

    AnimatedContent(
        targetState = displayedData,
        modifier = modifier.fillMaxWidth(),
        transitionSpec = {
            val enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(normalMillis, easing = standardEasing),
            ) + fadeIn(animationSpec = tween(fastMillis))
            val exit = slideOutVertically(
                targetOffsetY = { it + exitTravelDistance },
                animationSpec = tween(normalMillis, easing = standardEasing),
            ) + fadeOut(animationSpec = tween(normalMillis, easing = standardEasing))
            (enter togetherWith exit).using(SizeTransform(clip = false))
        },
        label = "IenSnackbarHost",
    ) { data ->
        if (data == null) {
            Box(modifier = Modifier.fillMaxWidth())
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IenTheme.spacing.lg),
                contentAlignment = Alignment.Center,
            ) {
                IenSnackbar(data = data)
            }
        }
    }
}

private fun SnackbarDuration.ienSnackbarDurationMillis(): Long? = when (this) {
    SnackbarDuration.Short -> 4_000L
    SnackbarDuration.Long -> 10_000L
    SnackbarDuration.Indefinite -> null
}

/**
 * Material [SnackbarData]를 IEN 스타일 스낵바 카드로 렌더링합니다.
 */
@Composable
fun IenSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current
    val visuals = data.visuals as? IenSnackbarVisuals
    val tone = visuals?.tone ?: IenSemanticTone.Neutral
    val offsetY = remember(data) { Animatable(0f) }
    val dismissThreshold = with(density) { IenSnackbarDefaults.SwipeDismissThreshold.toPx() }
    val exitTravelDistance = with(density) { IenSnackbarDefaults.ExitTravelDistance.toPx() }
    val normalMillis = IenTheme.motion.normalMillis
    val standardEasing = IenTheme.motion.standardEasing

    IenSnackbarContent(
        text = data.visuals.message,
        modifier = modifier
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
            .graphicsLayer {
                alpha = (1f - (offsetY.value / dismissThreshold).coerceIn(0f, 1f) * 0.35f)
            }
            .pointerInput(data) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        coroutineScope.launch {
                            if (offsetY.value > dismissThreshold) {
                                offsetY.animateTo(
                                    targetValue = size.height.toFloat() + exitTravelDistance,
                                    animationSpec = tween(normalMillis, easing = standardEasing),
                                )
                                data.dismiss()
                            } else {
                                offsetY.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(normalMillis, easing = standardEasing),
                                )
                            }
                        }
                    },
                    onDragCancel = {
                        coroutineScope.launch {
                            offsetY.animateTo(
                                targetValue = 0f,
                                animationSpec = tween(normalMillis, easing = standardEasing),
                            )
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            offsetY.snapTo((offsetY.value + dragAmount).coerceAtLeast(0f))
                        }
                    },
                )
            },
        leftAddon = if (tone == IenSemanticTone.Neutral) null else {
            { IenSnackbarIcon(tone = tone) }
        },
        actionLabel = data.visuals.actionLabel,
        onAction = data::performAction,
        minWidth = if (visuals != null) visuals.minWidth else IenSnackbarDefaults.MinWidth,
        maxWidth = visuals?.maxWidth ?: IenSnackbarDefaults.MaxWidth,
        fillMaxWidth = visuals?.fillMaxWidth ?: IenSnackbarDefaults.FillMaxWidth,
    )
}

@Composable
internal fun IenSnackbarContent(
    text: String,
    modifier: Modifier = Modifier,
    leftAddon: (@Composable () -> Unit)? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    minWidth: Dp? = IenSnackbarDefaults.MinWidth,
    maxWidth: Dp = IenSnackbarDefaults.MaxWidth,
    fillMaxWidth: Boolean = IenSnackbarDefaults.FillMaxWidth,
) {
    val container = Color(0xFF191F28)
    val sizeModifier = Modifier.widthIn(
        min = minWidth ?: Dp.Unspecified,
        max = maxWidth,
    ).then(
        if (fillMaxWidth) Modifier.fillMaxWidth() else Modifier
    )
    IenSurface(
        modifier = modifier
            .shadow(elevation = 6.dp, shape = ContinuousRoundedRectangle(IenTheme.radius.xl), clip = false)
            .then(sizeModifier)
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = text
            },
        color = container,
        contentColor = Color.White,
        shape = ContinuousRoundedRectangle(IenTheme.radius.xl),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = IenTheme.spacing.lg, vertical = IenTheme.spacing.md),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leftAddon?.invoke()
            Text(
                text = text,
                modifier = Modifier.weight(1f, fill = false),
                color = Color.White,
                style = IenTheme.typography.body2,
            )
            if (actionLabel != null && onAction != null) {
                IenSnackbarActionButton(text = actionLabel, onClick = onAction)
            }
        }
    }
}

/**
 * 스낵바 카드 우측에 들어갈 파란색 텍스트 액션 버튼 컴포저블입니다.
 *
 * @param text 버튼 텍스트
 * @param onClick 버튼 클릭 콜백 함수
 * @param modifier 적용할 [Modifier]
 */
@Composable
fun IenSnackbarActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier
            .clip(ContinuousRoundedRectangle(IenTheme.radius.sm))
            .clickable(onClick = onClick)
            .padding(horizontal = IenTheme.spacing.sm, vertical = IenTheme.spacing.xs),
        color = Color(0xFF3182F6),
        style = IenTheme.typography.label1.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
    )
}

/**
 * 스낵바 좌측에 시맨틱 톤에 따른 상태 아이콘을 나타내는 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param tone 나타낼 상태의 시맨틱 톤 ([IenSemanticTone])
 * @param size 아이콘 및 배경 서클의 크기
 */
@Composable
fun IenSnackbarIcon(
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Success,
    size: Dp = IenTheme.icon.md,
) {
    val color = when (tone) {
        IenSemanticTone.Neutral -> IenTheme.colors.surface
        IenSemanticTone.Brand -> IenTheme.colors.brand
        IenSemanticTone.Success -> IenTheme.colors.success
        IenSemanticTone.Warning -> IenTheme.colors.warning
        IenSemanticTone.Danger -> IenTheme.colors.danger
        IenSemanticTone.Info -> IenTheme.colors.info
    }
    val iconVector = when (tone) {
        IenSemanticTone.Success -> SystemIcons.Check
        IenSemanticTone.Danger -> SystemIcons.Close
        else -> SystemIcons.Check
    }
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center,
    ) {
        IenIcon(
            icon = IconData.Vector(iconVector),
            contentDescription = null,
            modifier = Modifier.size(size * 0.7f),
            tint = color,
        )
    }
}
