package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AVFAudio.AVAudioPlayer
import platform.Foundation.NSURL
import zone.ien.utils.cmp_ui.generated.resources.Res

private class IosIenWheelPickerSoundFeedback(
    private val hapticFeedback: HapticFeedback,
) : IenWheelPickerSoundFeedback {
    override fun playTick(enableHapticFeedback: Boolean) {
        if (enableHapticFeedback) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
        }
        IosWheelPickerTickPlayer.playTick()
    }
}

private object IosWheelPickerTickPlayer {
    private val player by lazy(::createTickPlayer)

    fun playTick() {
        player?.let {
            it.currentTime = 0.0
            it.play()
        }
    }
}

@Composable
internal actual fun rememberIenWheelPickerSoundFeedback(): IenWheelPickerSoundFeedback {
    val hapticFeedback = LocalHapticFeedback.current
    return remember(hapticFeedback) { IosIenWheelPickerSoundFeedback(hapticFeedback) }
}

@OptIn(ExperimentalForeignApi::class)
private fun createTickPlayer(): AVAudioPlayer? {
    val soundUrl = NSURL.URLWithString(Res.getUri("files/wheel_picker_tick.wav"))
        ?: return null
    return AVAudioPlayer(contentsOfURL = soundUrl, error = null).apply {
        volume = 0.2f
        prepareToPlay()
    }
}
