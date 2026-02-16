package zone.ien.utils.adaptive.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun dynamicM3ColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme {
    return when {
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