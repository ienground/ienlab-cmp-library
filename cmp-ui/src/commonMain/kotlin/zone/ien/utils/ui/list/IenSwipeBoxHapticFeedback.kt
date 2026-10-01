package zone.ien.utils.ui.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

/** `IenSwipeBox`에서 사용하는 플랫폼 공통 햅틱 의미입니다. */
internal enum class IenSwipeBoxHapticFeedbackType {
    ImpactLight,
}

/** Android의 공통 햅틱 타입으로 변환합니다. */
internal val IenSwipeBoxHapticFeedbackType.androidType: HapticFeedbackType
    get() = when (this) {
        IenSwipeBoxHapticFeedbackType.ImpactLight -> HapticFeedbackType.GestureThresholdActivate
    }

/** `IenSwipeBox`의 플랫폼별 햅틱 실행기입니다. */
internal interface IenSwipeBoxHapticFeedback {
    fun performImpactLight()
}

@Composable
internal expect fun rememberIenSwipeBoxHapticFeedback(): IenSwipeBoxHapticFeedback
