package zone.ien.utils.utils.ui

import androidx.compose.foundation.text.KeyboardOptions

// Android does not require additional configuration for native input.
actual fun KeyboardOptions.enableNativeInput(): KeyboardOptions = this
