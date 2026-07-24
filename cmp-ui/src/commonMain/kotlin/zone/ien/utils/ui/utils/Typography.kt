package zone.ien.utils.ui.utils

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.foundation.IenTypography

/**
 * getIenTypography은 현재 Ien 타이포그래피를 반환하는 함수입니다.
 * 
 * 이 함수는 IenTheme.typography를 기반으로 제공된 폰트 패밀리를 적용하여
 * Ien 타이포그래피를 반환합니다.
 * 
 * @param fontFamily 폰트 패밀리
 * @return Ien 타이포그래피
 */
@Composable
fun getIenTypography(fontFamily: FontFamily? = null): IenTypography {
    return IenTheme.typography.withFontFamily(fontFamily)
}

/**
 * getMaterialTypography은 Material Design 3 타이포그래피를 반환하는 함수입니다.
 *
 * 이 함수는 IenTheme.typography를 기반으로 제공된 폰트 패밀리를 적용한 뒤
 * Material Design 3 타이포그래피로 변환합니다.
 *
 * @param fontFamily 폰트 패밀리
 * @return M3 타이포그래피
 */
@Composable
fun getMaterialTypography(fontFamily: FontFamily? = null): Typography {
    return getIenTypography(fontFamily).toMaterialTypography()
}

/**
 * IenTypography의 모든 TextStyle에 폰트 패밀리를 적용합니다.
 *
 * @param fontFamily 적용할 폰트 패밀리
 * @return 폰트 패밀리가 적용된 Ien 타이포그래피
 */
fun IenTypography.withFontFamily(fontFamily: FontFamily? = null): IenTypography {
    fun TextStyle.withFontFamily(): TextStyle =
        fontFamily?.let { copy(fontFamily = it) } ?: this

    return IenTypography(
        display = display.withFontFamily(),
        title1 = title1.withFontFamily(),
        title2 = title2.withFontFamily(),
        title3 = title3.withFontFamily(),
        body1 = body1.withFontFamily(),
        body2 = body2.withFontFamily(),
        label1 = label1.withFontFamily(),
        label2 = label2.withFontFamily(),
        caption = caption.withFontFamily(),
    )
}

/**
 * IenTypography를 Material Design 3 타이포그래피로 변환합니다.
 *
 * @return M3 타이포그래피
 */
fun IenTypography.toMaterialTypography(): Typography {
    return Typography(
        displayLarge = display,
        displayMedium = display,
        displaySmall = title1,
        headlineLarge = title1,
        headlineMedium = title2,
        headlineSmall = title3,
        titleLarge = title2,
        titleMedium = title3,
        titleSmall = label1,
        bodyLarge = body1,
        bodyMedium = body2,
        bodySmall = caption,
        labelLarge = label1,
        labelMedium = label2,
        labelSmall = caption,
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
