package zone.ien.utils.ui.utils

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import zone.ien.utils.ui.components.foundation.IenTheme

/**
 * getM3Typography은 Material Design 3 타이포그래피를 반환하는 함수입니다.
 * 
 * 이 함수는 IenTheme.typography를 기반으로 제공된 폰트 패밀리를 적용하여
 * 타이포그래피를 반환합니다.
 * 
 * @param fontFamily 폰트 패밀리
 * @return M3 타이포그래피
 */
@Composable
fun getM3Typography(fontFamily: FontFamily? = null): Typography {
    fun TextStyle.withFontFamily(): TextStyle =
        fontFamily?.let { copy(fontFamily = it) } ?: this

    val typography = IenTheme.typography

    return Typography(
        displayLarge = typography.display.withFontFamily(),
        displayMedium = typography.display.withFontFamily(),
        displaySmall = typography.title1.withFontFamily(),
        headlineLarge = typography.title1.withFontFamily(),
        headlineMedium = typography.title2.withFontFamily(),
        headlineSmall = typography.title3.withFontFamily(),
        titleLarge = typography.title2.withFontFamily(),
        titleMedium = typography.title3.withFontFamily(),
        titleSmall = typography.label1.withFontFamily(),
        bodyLarge = typography.body1.withFontFamily(),
        bodyMedium = typography.body2.withFontFamily(),
        bodySmall = typography.caption.withFontFamily(),
        labelLarge = typography.label1.withFontFamily(),
        labelMedium = typography.label2.withFontFamily(),
        labelSmall = typography.caption.withFontFamily(),
    )
}

/**
 * TextStyle을 Thin 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Thin 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toThin() = this.copy(fontWeight = FontWeight.Thin)

/**
 * TextStyle을 ExtraLight 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return ExtraLight 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toExtraLight() = this.copy(fontWeight = FontWeight.ExtraLight)

/**
 * TextStyle을 Light 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Light 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toLight() = this.copy(fontWeight = FontWeight.Light)

/**
 * TextStyle을 Normal 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Normal 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toNormal() = this.copy(fontWeight = FontWeight.Normal)

/**
 * TextStyle을 Medium 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Medium 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toMedium() = this.copy(fontWeight = FontWeight.Medium)

/**
 * TextStyle을 SemiBold 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return SemiBold 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toSemiBold() = this.copy(fontWeight = FontWeight.SemiBold)

/**
 * TextStyle을 Bold 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Bold 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toBold() = this.copy(fontWeight = FontWeight.Bold)

/**
 * TextStyle을 ExtraBold 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return ExtraBold 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toExtraBold() = this.copy(fontWeight = FontWeight.ExtraBold)

/**
 * TextStyle을 Black 폰트 웨이트로 변환하는 확장 함수
 * 
 * @return Black 폰트 웨이트로 변환된 TextStyle
 */
fun TextStyle.toBlack() = this.copy(fontWeight = FontWeight.Black)
