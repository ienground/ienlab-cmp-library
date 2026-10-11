package zone.ien.utils.ui.interactive

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import zone.ien.utils.cmp_ui.generated.resources.Res

private class AndroidIenWheelPickerSoundFeedback(
    context: Context,
    private val hapticFeedback: HapticFeedback,
) : IenWheelPickerSoundFeedback {
    private val soundPool = SoundPool.Builder()
        .setMaxStreams(4)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build(),
        )
        .build()
    private var soundId = 0
    private var soundLoaded = false

    init {
        soundPool.setOnLoadCompleteListener { _, loadedSoundId, status ->
            if (loadedSoundId == soundId) soundLoaded = status == 0
        }
        val assetPath = Uri.parse(Res.getUri("files/wheel_picker_tick.wav"))
            .path
            ?.removePrefix("/android_asset/")
            ?: error("휠 피커 효과음 경로를 찾을 수 없습니다.")
        soundId = context.assets.openFd(assetPath).use { soundPool.load(it, 1) }
    }

    override fun playTick(enableHapticFeedback: Boolean) {
        if (enableHapticFeedback) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)
        }
        if (soundLoaded) soundPool.play(soundId, 0.2f, 0.2f, 1, 0, 1f)
    }

    fun release() = soundPool.release()
}

@Composable
internal actual fun rememberIenWheelPickerSoundFeedback(): IenWheelPickerSoundFeedback {
    val context = LocalContext.current
    val hapticFeedback = LocalHapticFeedback.current
    val feedback = remember(context, hapticFeedback) {
        AndroidIenWheelPickerSoundFeedback(context, hapticFeedback)
    }
    DisposableEffect(feedback) {
        onDispose(feedback::release)
    }
    return feedback
}
