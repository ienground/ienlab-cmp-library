package zone.ien.utils.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
fun getM3Typography(fontFamily: FontFamily? = null): Typography =
    MaterialTheme.typography.let {
        it.copy(
            displayLarge = fontFamily?.let { f -> it.displayLarge.copy(fontFamily = f) } ?: it.displayLarge,
            displayMedium = fontFamily?.let { f -> it.displayMedium.copy(fontFamily = f) } ?: it.displayMedium,
            displaySmall = fontFamily?.let { f -> it.displaySmall.copy(fontFamily = f) } ?: it.displaySmall,
            headlineLarge = fontFamily?.let { f -> it.headlineLarge.copy(fontFamily = f) } ?: it.headlineLarge,
            headlineMedium = fontFamily?.let { f -> it.headlineMedium.copy(fontFamily = f) } ?: it.headlineMedium,
            headlineSmall = fontFamily?.let { f -> it.headlineSmall.copy(fontFamily = f) } ?: it.headlineSmall,
            titleLarge = fontFamily?.let { f -> it.titleLarge.copy(fontFamily = f) } ?: it.titleLarge,
            titleMedium = fontFamily?.let { f -> it.titleMedium.copy(fontFamily = f) } ?: it.titleMedium,
            titleSmall = fontFamily?.let { f -> it.titleSmall.copy(fontFamily = f) } ?: it.titleSmall,
            bodyLarge = fontFamily?.let { f -> it.bodyLarge.copy(fontFamily = f) } ?: it.bodyLarge,
            bodyMedium = fontFamily?.let { f -> it.bodyMedium.copy(fontFamily = f) } ?: it.bodyMedium,
            bodySmall = fontFamily?.let { f -> it.bodySmall.copy(fontFamily = f) } ?: it.bodySmall,
            labelLarge = fontFamily?.let { f -> it.labelLarge.copy(fontFamily = f) } ?: it.labelLarge,
            labelMedium = fontFamily?.let { f -> it.labelMedium.copy(fontFamily = f) } ?: it.labelMedium,
            labelSmall = fontFamily?.let { f -> it.labelSmall.copy(fontFamily = f) } ?: it.labelSmall
        )
    }

@Composable fun TextStyle.toThin() = this.copy(fontWeight = FontWeight.Thin)
@Composable fun TextStyle.toExtraLight() = this.copy(fontWeight = FontWeight.ExtraLight)
@Composable fun TextStyle.toLight() = this.copy(fontWeight = FontWeight.Light)
@Composable fun TextStyle.toNormal() = this.copy(fontWeight = FontWeight.Normal)
@Composable fun TextStyle.toMedium() = this.copy(fontWeight = FontWeight.Medium)
@Composable fun TextStyle.toSemiBold() = this.copy(fontWeight = FontWeight.SemiBold)
@Composable fun TextStyle.toBold() = this.copy(fontWeight = FontWeight.Bold)
@Composable fun TextStyle.toExtraBold() = this.copy(fontWeight = FontWeight.ExtraBold)
@Composable fun TextStyle.toBlack() = this.copy(fontWeight = FontWeight.Black)