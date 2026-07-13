package zone.ien.utils.ui.foundation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class IenColorScheme(
    val background: Color,
    val surface: Color,
    val surfaceRaised: Color,
    val surfaceWeak: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val border: Color,
    val borderStrong: Color,
    val overlay: Color,
    val surfaceVariant: Color,

    // Brand
    val brand: Color,
    val onBrand: Color,
    val brandWeak: Color,
    val onBrandWeak: Color,

    // Success
    val success: Color,
    val onSuccess: Color,
    val successWeak: Color,
    val onSuccessWeak: Color,

    // Warning
    val warning: Color,
    val onWarning: Color,
    val warningWeak: Color,
    val onWarningWeak: Color,

    // Danger
    val danger: Color,
    val onDanger: Color,
    val dangerWeak: Color,
    val onDangerWeak: Color,

    // Info
    val info: Color,
    val onInfo: Color,
    val infoWeak: Color,
    val onInfoWeak: Color,
)

@Immutable
data class IenTypography(
    val display: TextStyle,
    val title1: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val caption: TextStyle,
)

@Immutable
data class IenSpacing(
    val none: Dp = 0.dp,
    val xxxs: Dp = 2.dp,
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp,
    val xxxl: Dp = 40.dp,
)

@Immutable
data class IenRadius(
    val none: Dp = 0.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val default: Dp = 12.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val xl: Dp = 24.dp,
    val full: Dp = 999.dp,
)

@Immutable
data class IenStroke(
    val hairline: Dp = 0.5.dp,
    val thin: Dp = 1.dp,
    val medium: Dp = 1.5.dp,
    val thick: Dp = 2.dp,
)

@Immutable
data class IenElevation(
    val none: Dp = 0.dp,
    val raised: Dp = 4.dp,
    val floating: Dp = 12.dp,
    val overlay: Dp = 24.dp,
)

@Immutable
data class IenIconSize(
    val xs: Dp = 12.dp,
    val sm: Dp = 16.dp,
    val md: Dp = 20.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

@Immutable
data class IenMotion(
    val instantMillis: Int = 80,
    val fastMillis: Int = 160,
    val normalMillis: Int = 240,
    val slowMillis: Int = 360,
    val standardEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
)

@Immutable
data class IenStateTokens(
    val disabledAlpha: Float = 0.38f,
    val pressedAlpha: Float = 0.10f,
    val focusedAlpha: Float = 0.12f,
    val selectedAlpha: Float = 0.14f,
    val minimumTouchTarget: Dp = 44.dp,
)

@Immutable
data class IenTokens(
    val colors: IenColorScheme,
    val typography: IenTypography,
    val spacing: IenSpacing = IenSpacing(),
    val radius: IenRadius = IenRadius(),
    val stroke: IenStroke = IenStroke(),
    val elevation: IenElevation = IenElevation(),
    val icon: IenIconSize = IenIconSize(),
    val motion: IenMotion = IenMotion(),
    val state: IenStateTokens = IenStateTokens(),
)

val LocalIenTokens = staticCompositionLocalOf { lightIenTokens() }

object IenTheme {
    val colors: IenColorScheme
        @Composable get() = LocalIenTokens.current.colors
    val typography: IenTypography
        @Composable get() = LocalIenTokens.current.typography
    val spacing: IenSpacing
        @Composable get() = LocalIenTokens.current.spacing
    val radius: IenRadius
        @Composable get() = LocalIenTokens.current.radius
    val stroke: IenStroke
        @Composable get() = LocalIenTokens.current.stroke
    val elevation: IenElevation
        @Composable get() = LocalIenTokens.current.elevation
    val icon: IenIconSize
        @Composable get() = LocalIenTokens.current.icon
    val motion: IenMotion
        @Composable get() = LocalIenTokens.current.motion
    val state: IenStateTokens
        @Composable get() = LocalIenTokens.current.state
}

@Composable
fun IenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    tokens: IenTokens? = null,
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalIenTokens provides (tokens ?: if (darkTheme) darkIenTokens() else lightIenTokens()),
        content = content,
    )
}

fun lightIenTokens() = IenTokens(
    colors = IenColorScheme(
        background = Color(0xFFFFFFFF),
        surface = Color(0xFFFFFFFF),
        surfaceRaised = Color(0xFFFFFFFF),
        surfaceWeak = Color(0xFFF9FAFB),
        textPrimary = Color(0xFF191F28),
        textSecondary = Color(0xFF4E5968),
        textTertiary = Color(0xFF8B95A1),
        textDisabled = Color(0xFFB0B8C1),
        border = Color(0xFFE5E8EB),
        borderStrong = Color(0xFFD1D6DB),
        overlay = Color(0x99000000),
        surfaceVariant = Color(0xFFF2F4F6),
        brand = Color(0xFF3182F6),
        onBrand = Color(0xFFFFFFFF),
        brandWeak = Color(0xFFE8F3FF),
        onBrandWeak = Color(0xFF3182F6),
        success = Color(0xFF03B26C),
        onSuccess = Color(0xFFFFFFFF),
        successWeak = Color(0xFFF0FAF6),
        onSuccessWeak = Color(0xFF03B26C),
        warning = Color(0xFFFE9800),
        onWarning = Color(0xFFFFFFFF),
        warningWeak = Color(0xFFFFF3E0),
        onWarningWeak = Color(0xFFFE9800),
        danger = Color(0xFFF04452),
        onDanger = Color(0xFFFFFFFF),
        dangerWeak = Color(0xFFFFEEEE),
        onDangerWeak = Color(0xFFF04452),
        info = Color(0xFF18A5A5),
        onInfo = Color(0xFFFFFFFF),
        infoWeak = Color(0xFFEDF8F8),
        onInfoWeak = Color(0xFF18A5A5),
    ),
    typography = defaultIenTypography(),
)

fun darkIenTokens() = IenTokens(
    colors = IenColorScheme(
        background = Color(0xFF101318),
        surface = Color(0xFF171B22),
        surfaceRaised = Color(0xFF202631),
        surfaceWeak = Color(0xFF11151B),
        textPrimary = Color(0xFFF2F4F6),
        textSecondary = Color(0xFFD1D6DB),
        textTertiary = Color(0xFF8B95A1),
        textDisabled = Color(0xFF6B7684),
        border = Color(0xFF333D4B),
        borderStrong = Color(0xFF4E5968),
        overlay = Color(0xB3000000),
        surfaceVariant = Color(0xFF202632),
        brand = Color(0xFF64A8FF),
        onBrand = Color(0xFFFFFFFF),
        brandWeak = Color(0xFF17365D),
        onBrandWeak = Color(0xFF64A8FF),
        success = Color(0xFF3FD599),
        onSuccess = Color(0xFFFFFFFF),
        successWeak = Color(0xFF113B2B),
        onSuccessWeak = Color(0xFF3FD599),
        warning = Color(0xFFFFBD51),
        onWarning = Color(0xFFFFFFFF),
        warningWeak = Color(0xFF4A3211),
        onWarningWeak = Color(0xFFFFBD51),
        danger = Color(0xFFFB8890),
        onDanger = Color(0xFFFFFFFF),
        dangerWeak = Color(0xFF4A1D22),
        onDangerWeak = Color(0xFFFB8890),
        info = Color(0xFF58C7C7),
        onInfo = Color(0xFFFFFFFF),
        infoWeak = Color(0xFF123A3A),
        onInfoWeak = Color(0xFF58C7C7),
    ),
    typography = defaultIenTypography(),
)

private fun defaultIenTypography() = IenTypography(
    display = TextStyle(fontSize = 30.sp, lineHeight = 38.sp, fontWeight = FontWeight.Bold),
    title1 = TextStyle(fontSize = 24.sp, lineHeight = 32.sp, fontWeight = FontWeight.Bold),
    title2 = TextStyle(fontSize = 20.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold),
    title3 = TextStyle(fontSize = 18.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    body1 = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    body2 = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal),
    label1 = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.SemiBold),
    label2 = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.SemiBold),
    caption = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Normal),
)
