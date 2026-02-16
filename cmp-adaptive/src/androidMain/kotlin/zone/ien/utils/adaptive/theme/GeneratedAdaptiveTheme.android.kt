package zone.ien.utils.adaptive.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun dynamicM3ColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme {
    return when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkScheme
        else -> lightScheme
    }
}

@Composable
actual fun dynamicCupertinoColorScheme(
    darkTheme: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): zone.ien.hig.theme.ColorScheme {
    return if (darkTheme) zone.ien.hig.theme.darkColorScheme(accent = darkScheme.primary)
    else zone.ien.hig.theme.lightColorScheme(accent = lightScheme.primary)
}