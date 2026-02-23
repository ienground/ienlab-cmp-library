package zone.ien.utils.adaptive.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color
import zone.ien.hig.adaptive.AdaptiveTheme
import zone.ien.hig.adaptive.CupertinoThemeSpec
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.MaterialThemeSpec
import zone.ien.hig.adaptive.Theme

@Composable
expect fun dynamicM3ColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme

@Composable
expect fun dynamicCupertinoColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): zone.ien.hig.theme.ColorScheme

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun GeneratedAdaptiveTheme(
    target: Theme,
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    shapes: zone.ien.hig.adaptive.Shapes = zone.ien.hig.adaptive.Shapes(),
    lightScheme: ColorScheme = lightColorScheme(),
    darkScheme: ColorScheme = darkColorScheme(),
    materialTypography: Typography = Typography(),
    cupertinoTypography: zone.ien.hig.theme.Typography = zone.ien.hig.theme.Typography(),
    vararg values: ProvidedValue<*>,
    content: @Composable () -> Unit
) {
    AdaptiveTheme(
        target = target,
        material = MaterialThemeSpec(
            colorScheme = dynamicM3ColorScheme(
                darkTheme = useDarkTheme,
                dynamicColor = useDynamicColor,
                lightScheme = lightScheme,
                darkScheme = darkScheme
            ),
            shapes = Shapes(
                extraSmall = shapes.extraSmall,
                small = shapes.small,
                medium =  shapes.medium,
                large = shapes.large,
                extraLarge = shapes.extraLarge
            ),
            typography = materialTypography
        ),
        cupertino = CupertinoThemeSpec(
            colorScheme = dynamicCupertinoColorScheme(
                darkTheme = useDarkTheme,
                lightScheme = lightScheme,
                darkScheme = darkScheme
            ),
            shapes = zone.ien.hig.theme.Shapes(
                extraSmall = shapes.higExtraSmall,
                small = shapes.higSmall,
                medium = shapes.higMedium,
                large = shapes.higLarge,
                extraLarge = shapes.higExtraLarge
            ),
            typography = cupertinoTypography
        ),
        content = {
            CompositionLocalProvider(values = values) {
                content()
            }
        }
    )
}