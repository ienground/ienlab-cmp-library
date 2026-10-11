package zone.ien.utils.ui.interactive

import androidx.compose.runtime.Composable
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

private object BrowserIenWheelPickerSoundFeedback : IenWheelPickerSoundFeedback {
    override fun playTick(enableHapticFeedback: Boolean) = playBrowserWheelPickerTick()
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun playBrowserWheelPickerTick() {
    js("""(function() {
            const AudioContext = window.AudioContext || window.webkitAudioContext;
            if (!AudioContext) return;

            const context = globalThis.__ienWheelPickerAudioContext ||
                (globalThis.__ienWheelPickerAudioContext = new AudioContext());
            const play = function() {
                const oscillator = context.createOscillator();
                const volume = context.createGain();
                const endTime = context.currentTime + 0.03;
                oscillator.type = 'sine';
                oscillator.frequency.setValueAtTime(1100, context.currentTime);
                oscillator.frequency.exponentialRampToValueAtTime(700, endTime);
                volume.gain.setValueAtTime(0.08, context.currentTime);
                volume.gain.exponentialRampToValueAtTime(0.001, endTime);
                oscillator.connect(volume);
                volume.connect(context.destination);
                oscillator.start();
                oscillator.stop(endTime);
            };

            if (context.state === 'suspended') context.resume().then(play);
            else play();
        })()""")
}

@Composable
internal actual fun rememberIenWheelPickerSoundFeedback(): IenWheelPickerSoundFeedback =
    BrowserIenWheelPickerSoundFeedback
