package zone.ien.utils.adaptive.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.IenTokens
import zone.ien.utils.ui.foundation.darkIenTokens
import zone.ien.utils.ui.foundation.lightIenTokens

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun IenAdaptiveTheme(
    target: Theme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    useDynamicColor: Boolean = false,
    lightTokens: IenTokens = lightIenTokens(),
    darkTokens: IenTokens = darkIenTokens(),
    materialTypography: Typography = Typography(),
    cupertinoTypography: zone.ien.hig.theme.Typography = zone.ien.hig.theme.Typography(),
    vararg values: ProvidedValue<*>,
    content: @Composable () -> Unit,
) {
    val tokens = if (darkTheme) darkTokens else lightTokens
    IenTheme(tokens = tokens) {
        GeneratedAdaptiveTheme(
            target = target,
            useDarkTheme = darkTheme,
            useDynamicColor = useDynamicColor,
            lightScheme = lightTokens.colors.toIenMaterialColorScheme(darkTheme = false),
            darkScheme = darkTokens.colors.toIenMaterialColorScheme(darkTheme = true),
            materialTypography = materialTypography,
            cupertinoTypography = cupertinoTypography,
            values = values,
            content = content,
        )
    }
}

fun IenColorScheme.toIenMaterialColorScheme(darkTheme: Boolean): ColorScheme {
    val neutralInverse = if (darkTheme) surface else textPrimary
    return if (darkTheme) {
        darkColorScheme(
            primary = brand,
            onPrimary = onBrand,
            primaryContainer = brandWeak,
            onPrimaryContainer = onBrandWeak,
            secondary = info,
            onSecondary = onInfo,
            secondaryContainer = infoWeak,
            onSecondaryContainer = onInfoWeak,
            error = danger,
            onError = onDanger,
            errorContainer = dangerWeak,
            onErrorContainer = onDangerWeak,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = textSecondary,
            outline = border,
            outlineVariant = borderStrong,
            inverseSurface = neutralInverse,
            inverseOnSurface = background,
            inversePrimary = brandWeak,
            surfaceTint = surface,
        )
    } else {
        lightColorScheme(
            primary = brand,
            onPrimary = onBrand,
            primaryContainer = brandWeak,
            onPrimaryContainer = onBrandWeak,
            secondary = info,
            onSecondary = onInfo,
            secondaryContainer = infoWeak,
            onSecondaryContainer = onInfoWeak,
            error = danger,
            onError = onDanger,
            errorContainer = dangerWeak,
            onErrorContainer = onDangerWeak,
            background = background,
            onBackground = textPrimary,
            surface = surface,
            onSurface = textPrimary,
            surfaceVariant = surfaceVariant,
            onSurfaceVariant = textSecondary,
            outline = border,
            outlineVariant = borderStrong,
            inverseSurface = neutralInverse,
            inverseOnSurface = background,
            inversePrimary = brandWeak,
            surfaceTint = surface,
        )
    }
}
