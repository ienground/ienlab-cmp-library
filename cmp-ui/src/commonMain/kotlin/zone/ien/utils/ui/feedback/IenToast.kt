package zone.ien.utils.ui.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme

/**
 * IEN 토스트가 화면에 머무르는 시간을 정의합니다.
 */
enum class IenToastDuration(
    val durationMillis: Long?,
) {
    Short(4_000L),
    Long(10_000L),
    Indefinite(null),
}

/**
 * IEN 토스트에 표시할 메시지와 시각 정보를 담는 데이터입니다.
 */
@Immutable
class IenToastData internal constructor(
    val message: String,
    val tone: IenSemanticTone,
    val duration: IenToastDuration,
    internal val id: Long,
)

/**
 * 전역 토스트의 표시 상태를 관리합니다.
 */
@Stable
class IenToastState internal constructor() {
    var currentToast by mutableStateOf<IenToastData?>(null)
        private set

    private var nextToastId = 0L

    /**
     * 새 토스트를 표시합니다. 이미 표시 중인 토스트는 새 토스트로 교체됩니다.
     */
    fun show(
        message: String,
        tone: IenSemanticTone = IenSemanticTone.Neutral,
        duration: IenToastDuration = IenToastDuration.Short,
    ) {
        currentToast = IenToastData(
            message = message,
            tone = tone,
            duration = duration,
            id = nextToastId++,
        )
    }

    /**
     * 현재 토스트를 즉시 닫습니다.
     */
    fun dismiss() {
        currentToast = null
    }
}

/**
 * [IenToastState]를 생성하고 기억합니다.
 */
@Composable
fun rememberIenToastState(): IenToastState = remember { IenToastState() }

/**
 * 하위 화면에서 전역 토스트 상태를 읽기 위한 CompositionLocal입니다.
 */
val LocalIenToastState = staticCompositionLocalOf<IenToastState?> { null }

/**
 * [IenToastState]에 IEN 토스트를 표시합니다.
 */
fun IenToastState.showIenToast(
    message: String,
    tone: IenSemanticTone = IenSemanticTone.Neutral,
    duration: IenToastDuration = IenToastDuration.Short,
) {
    show(message = message, tone = tone, duration = duration)
}

/**
 * 하위 콘텐츠 어디서나 토스트를 표시할 수 있도록 상태와 호스트를 연결합니다.
 */
@Composable
fun IenToastProvider(
    state: IenToastState = rememberIenToastState(),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalIenToastState provides state) {
            content()
        }
        IenToastHost(
            state = state,
            modifier = Modifier.matchParentSize(),
        )
    }
}

/**
 * 전역 토스트를 화면 하단에 표시하는 호스트입니다.
 */
@Composable
fun IenToastHost(
    state: IenToastState,
    modifier: Modifier = Modifier,
) {
    var displayedToast by remember { mutableStateOf<IenToastData?>(null) }
    val currentToast = state.currentToast
    val normalMillis = IenTheme.motion.normalMillis
    val fastMillis = IenTheme.motion.fastMillis
    val standardEasing = IenTheme.motion.standardEasing

    LaunchedEffect(currentToast) {
        if (currentToast != null) {
            displayedToast = currentToast
        } else {
            withFrameMillis { }
            if (state.currentToast == null) {
                displayedToast = null
            }
        }
    }

    LaunchedEffect(currentToast?.id) {
        val toast = currentToast ?: return@LaunchedEffect
        val durationMillis = toast.duration.durationMillis ?: return@LaunchedEffect
        delay(durationMillis)
        if (state.currentToast === toast) {
            state.dismiss()
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter,
    ) {
        AnimatedContent(
            targetState = displayedToast,
            transitionSpec = {
                val enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(normalMillis, easing = standardEasing),
                ) + fadeIn(animationSpec = tween(fastMillis))
                val exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(normalMillis, easing = standardEasing),
                ) + fadeOut(animationSpec = tween(normalMillis, easing = standardEasing))
                (enter togetherWith exit).using(SizeTransform(clip = false))
            },
            label = "IenToastHost",
        ) { toast ->
            if (toast == null) {
                Box(modifier = Modifier.fillMaxWidth())
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(
                            horizontal = IenTheme.spacing.lg,
                            vertical = IenTheme.spacing.lg,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    IenToast(data = toast)
                }
            }
        }
    }
}

/**
 * IEN 스타일 토스트 카드를 렌더링합니다.
 */
@Composable
fun IenToast(
    data: IenToastData,
    modifier: Modifier = Modifier,
) {
    IenSnackbarContent(
        text = data.message,
        modifier = modifier,
        leftAddon = if (data.tone == IenSemanticTone.Neutral) null else {
            { IenSnackbarIcon(tone = data.tone) }
        },
        maxWidth = IenSnackbarDefaults.MaxWidth,
    )
}
