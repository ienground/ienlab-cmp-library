package zone.ien.utils.ui.components.composite

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.components.foundation.IenSemanticTone
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenLoaderPrimitive
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

enum class IenSheetDetent { Content, Medium, Full }

@Stable
class IenBottomSheetState internal constructor(
    visible: Boolean,
    detent: IenSheetDetent,
) {
    var visible by mutableStateOf(visible)
        private set
    var detent by mutableStateOf(detent)
        private set

    fun show(detent: IenSheetDetent = IenSheetDetent.Content) {
        this.detent = detent
        visible = true
    }

    fun hide() {
        visible = false
    }
}

@Composable
fun rememberIenBottomSheetState(
    visible: Boolean = false,
    detent: IenSheetDetent = IenSheetDetent.Content,
) = remember { IenBottomSheetState(visible, detent) }

@Composable
fun IenBottomSheet(
    state: IenBottomSheetState,
    modifier: Modifier = Modifier,
    dismissOnScrimClick: Boolean = true,
    showDragHandle: Boolean = true,
    title: (@Composable () -> Unit)? = null,
    actions: (@Composable RowScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = state.visible,
        enter = fadeIn(tween(IenTheme.motion.fastMillis)),
        exit = fadeOut(tween(IenTheme.motion.fastMillis)),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(IenTheme.colors.overlay)
                .clickable(
                    enabled = dismissOnScrimClick,
                    indication = null,
                    interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                ) { state.hide() },
            contentAlignment = Alignment.BottomCenter,
        ) {
            IenSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        when (state.detent) {
                            IenSheetDetent.Content -> Modifier
                            IenSheetDetent.Medium -> Modifier.fillMaxHeight(0.55f)
                            IenSheetDetent.Full -> Modifier.fillMaxHeight(0.92f)
                        },
                    )
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                    ) { },
                color = IenTheme.colors.surfaceRaised,
                shape = RoundedCornerShape(topStart = IenTheme.radius.xl, topEnd = IenTheme.radius.xl),
            ) {
                Column(
                    modifier = Modifier.padding(IenTheme.spacing.lg),
                    verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
                ) {
                    if (showDragHandle) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .width(36.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(IenTheme.radius.full))
                                .background(IenTheme.colors.borderStrong),
                        )
                    }
                    title?.invoke()
                    content()
                    if (actions != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
                            content = actions,
                        )
                    }
                }
            }
        }
    }
}

@Immutable
data class IenDialogAction(
    val text: String,
    val onClick: () -> Unit,
    val tone: IenSemanticTone = IenSemanticTone.Brand,
)

@Composable
fun IenDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirm: IenDialogAction,
    modifier: Modifier = Modifier,
    dismiss: IenDialogAction? = null,
) {
    if (!visible) return
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { IenText(title, style = IenTheme.typography.title3) },
        text = { IenText(message, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary) },
        confirmButton = {
            TextButton(onClick = confirm.onClick) {
                IenText(confirm.text, style = IenTheme.typography.label1, color = IenTheme.colors.brand)
            }
        },
        dismissButton = dismiss?.let { action ->
            {
                TextButton(onClick = action.onClick) {
                    IenText(action.text, style = IenTheme.typography.label1, color = IenTheme.colors.textSecondary)
                }
            }
        },
    )
}

@Stable
class IenToastHostState {
    internal val toasts = mutableStateListOf<IenToastData>()

    fun show(message: String, tone: IenSemanticTone = IenSemanticTone.Neutral) {
        toasts.add(IenToastData(message, tone))
    }

    fun dismiss(data: IenToastData) {
        toasts.remove(data)
    }
}

@Immutable
data class IenToastData(
    val message: String,
    val tone: IenSemanticTone,
)

@Composable
fun IenToast(
    message: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
) {
    val container = when (tone) {
        IenSemanticTone.Neutral -> IenTheme.colors.textPrimary
        IenSemanticTone.Brand -> IenTheme.colors.brand
        IenSemanticTone.Success -> IenTheme.colors.success
        IenSemanticTone.Warning -> IenTheme.colors.warning
        IenSemanticTone.Danger -> IenTheme.colors.danger
        IenSemanticTone.Info -> IenTheme.colors.info
    }
    IenSurface(
        modifier = modifier.semantics {
            liveRegion = LiveRegionMode.Polite
            contentDescription = message
        },
        color = container,
        contentColor = IenTheme.colors.surface,
        shape = RoundedCornerShape(IenTheme.radius.default),
    ) {
        IenText(
            text = message,
            modifier = Modifier.padding(horizontal = IenTheme.spacing.md, vertical = IenTheme.spacing.sm),
            color = IenTheme.colors.surface,
            style = IenTheme.typography.body2,
        )
    }
}

@Composable
fun rememberIenToastHostState() = remember { IenToastHostState() }

@Composable
fun IenToastHost(
    state: IenToastHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(IenTheme.spacing.lg),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs)) {
            state.toasts.takeLast(3).forEach { toast ->
                IenToast(message = toast.message, tone = toast.tone)
            }
        }
    }
}

@Composable
fun IenSkeleton(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    radius: Dp = IenTheme.radius.sm,
) {
    val transition = rememberInfiniteTransition(label = "ienSkeleton")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.75f,
        animationSpec = infiniteRepeatable(
            animation = tween(IenTheme.motion.slowMillis),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ienSkeletonAlpha",
    )
    Box(
        modifier = modifier
            .height(height)
            .clip(RoundedCornerShape(radius))
            .background(IenTheme.colors.border.copy(alpha = alpha)),
    )
}

@Composable
fun IenProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    showLabel: Boolean = false,
) {
    val safeProgress = progress.coerceIn(0f, 1f)
    Column(
        modifier = modifier.semantics {
            if (contentDescription != null) this.contentDescription = contentDescription
            progressBarRangeInfo = androidx.compose.ui.semantics.ProgressBarRangeInfo(safeProgress, 0f..1f)
        },
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
    ) {
        LinearProgressIndicator(
            progress = { safeProgress },
            modifier = Modifier.fillMaxWidth(),
            color = IenTheme.colors.brand,
            trackColor = IenTheme.colors.brandWeak,
        )
        if (showLabel) {
            IenText(
                text = "${(safeProgress * 100).toInt()}%",
                style = IenTheme.typography.caption,
                color = IenTheme.colors.textSecondary,
            )
        }
    }
}

enum class IenLoaderSize { Small, Medium, Large }

@Composable
fun IenLoader(
    modifier: Modifier = Modifier,
    size: IenLoaderSize = IenLoaderSize.Medium,
    label: String? = null,
) {
    Column(
        modifier = modifier.semantics {
            contentDescription = label ?: "로딩 중"
            liveRegion = LiveRegionMode.Polite
        },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
    ) {
        IenLoaderPrimitive(
            modifier = Modifier.size(
                when (size) {
                    IenLoaderSize.Small -> IenTheme.icon.sm
                    IenLoaderSize.Medium -> IenTheme.icon.lg
                    IenLoaderSize.Large -> IenTheme.icon.xl
                },
            ),
            color = IenTheme.colors.brand,
        )
        if (label != null) {
            IenText(label, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
    }
}

enum class IenStepStatus { Pending, Current, Done, Error }

@Immutable
data class IenProgressStep(
    val label: String,
    val status: IenStepStatus,
)

@Composable
fun IenProgressStepper(
    steps: List<IenProgressStep>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "진행 단계 ${steps.count { it.status == IenStepStatus.Done }} / ${steps.size}"
            },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.Top,
    ) {
        steps.forEachIndexed { index, step ->
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val color = when (step.status) {
                    IenStepStatus.Pending -> IenTheme.colors.borderStrong
                    IenStepStatus.Current -> IenTheme.colors.brand
                    IenStepStatus.Done -> IenTheme.colors.success
                    IenStepStatus.Error -> IenTheme.colors.danger
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(color),
                    )
                    if (index != steps.lastIndex) {
                        Spacer(
                            modifier = Modifier
                                .height(IenTheme.stroke.thin)
                                .weight(1f)
                                .background(IenTheme.colors.border),
                        )
                    }
                }
                Spacer(Modifier.height(IenTheme.spacing.xs))
                IenText(
                    text = step.label,
                    style = IenTheme.typography.caption,
                    color = color,
                    maxLines = 2,
                )
            }
        }
    }
}

enum class IenResultTone { Success, Failure, Empty, Info }

@Composable
fun IenResult(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    tone: IenResultTone = IenResultTone.Info,
    icon: (@Composable BoxScope.() -> Unit)? = null,
    primaryAction: (@Composable () -> Unit)? = null,
    secondaryAction: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(IenTheme.spacing.xl),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md),
    ) {
        val iconColor = when (tone) {
            IenResultTone.Success -> IenTheme.colors.successWeak
            IenResultTone.Failure -> IenTheme.colors.dangerWeak
            IenResultTone.Empty -> IenTheme.colors.surfaceWeak
            IenResultTone.Info -> IenTheme.colors.infoWeak
        }
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(iconColor),
            contentAlignment = Alignment.Center,
        ) {
            icon?.invoke(this)
        }
        IenText(title, style = IenTheme.typography.title2)
        if (description != null) {
            IenText(description, style = IenTheme.typography.body2, color = IenTheme.colors.textSecondary)
        }
        primaryAction?.invoke()
        secondaryAction?.invoke()
    }
}
