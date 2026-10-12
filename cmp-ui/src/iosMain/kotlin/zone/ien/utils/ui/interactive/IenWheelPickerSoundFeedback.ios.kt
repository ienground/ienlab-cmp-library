package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import cnames.structs.__CFURL
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.UIntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.interpretCPointer
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.AudioToolbox.AudioServicesCreateSystemSoundID
import platform.AudioToolbox.AudioServicesPlaySystemSound
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
    private val soundId by lazy(::createTickSound)

    fun playTick() {
        soundId?.let { AudioServicesPlaySystemSound(it) }
    }
}

@Composable
internal actual fun rememberIenWheelPickerSoundFeedback(): IenWheelPickerSoundFeedback {
    val hapticFeedback = LocalHapticFeedback.current
    return remember(hapticFeedback) { IosIenWheelPickerSoundFeedback(hapticFeedback) }
}

@OptIn(ExperimentalForeignApi::class)
private fun createTickSound(): UInt? {
    val soundUrl = NSURL.URLWithString(Res.getUri("files/wheel_picker_tick.wav"))
        ?: return null

    return memScoped {
        val soundId = alloc<UIntVar>()
        // NSURL과 CFURL은 toll-free bridging 관계입니다.
        val cfUrl = interpretCPointer<__CFURL>(soundUrl.objcPtr()) ?: return@memScoped null
        val status = AudioServicesCreateSystemSoundID(cfUrl, soundId.ptr)
        if (status == 0) soundId.value else null
    }
}
