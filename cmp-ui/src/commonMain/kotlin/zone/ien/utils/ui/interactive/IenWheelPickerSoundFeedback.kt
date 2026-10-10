package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composable

internal interface IenWheelPickerSoundFeedback {
    fun playTick(enableHapticFeedback: Boolean)
}

@Composable
internal expect fun rememberIenWheelPickerSoundFeedback(): IenWheelPickerSoundFeedback
