package zone.ien.utils.utils.ui

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.text.input.PlatformImeOptions

/**
 * Enables native input for keyboard options on iOS.
 *
 * This function enables native input handling for iOS platforms.
 *
 * @return The [KeyboardOptions] with native input enabled
 */
@OptIn(ExperimentalComposeUiApi::class)
actual fun KeyboardOptions.enableNativeInput() = copy(platformImeOptions = PlatformImeOptions { usingNativeTextInput(true) })