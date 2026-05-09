package zone.ien.utils.ui.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/**
 * getM3Typography은 Material Design 3 타이포그래피를 반환하는 함수입니다.
 *
 * @param fontFamily 폰트 패밀리
 * @return M3 타이포그래피
 */
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

/**
 * TextStyle을 Thin 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toThin() = this.copy(fontWeight = FontWeight.Thin)

/**
 * TextStyle을 ExtraLight 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toExtraLight() = this.copy(fontWeight = FontWeight.ExtraLight)

/**
 * TextStyle을 Light 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toLight() = this.copy(fontWeight = FontWeight.Light)

/**
 * TextStyle을 Normal 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toNormal() = this.copy(fontWeight = FontWeight.Normal)

/**
 * TextStyle을 Medium 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toMedium() = this.copy(fontWeight = FontWeight.Medium)

/**
 * TextStyle을 SemiBold 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toSemiBold() = this.copy(fontWeight = FontWeight.SemiBold)

/**
 * TextStyle을 Bold 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toBold() = this.copy(fontWeight = FontWeight.Bold)

/**
 * TextStyle을 ExtraBold 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toExtraBold() = this.copy(fontWeight = FontWeight.ExtraBold)

/**
 * TextStyle을 Black 폰트 웨이트로 변환하는 확장 함수
 */
fun TextStyle.toBlack() = this.copy(fontWeight = FontWeight.Black)