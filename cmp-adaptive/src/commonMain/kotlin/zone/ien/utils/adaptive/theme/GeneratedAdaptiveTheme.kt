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

/**
 * 동적 M3 색상 스키마를 생성하는 함수
 * 
 * 이 함수는 시스템의 동적 색상 기능을 사용하여
 * 어두운/밝은 모드에 맞춘 색상 스키마를 반환합니다.
 * 
 * @param darkTheme 어두운 테마 사용 여부
 * @param dynamicColor 동적 색상 사용 여부 (Android 12 이상에서만 작동)
 * @param lightScheme 밝은 테마용 색상 스키마
 * @param darkScheme 어두운 테마용 색상 스키마
 * @return 동적으로 생성된 ColorScheme
 */
@Composable
expect fun dynamicM3ColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): ColorScheme

/**
 * 동적 Cupertino 색상 스키마를 생성하는 함수
 * 
 * 이 함수는 시스템의 동적 색상 기능을 사용하여
 * 어두운/밝은 모드에 맞춘 색상 스키마를 반환합니다.
 * 
 * @param darkTheme 어두운 테마 사용 여부
 * @param lightScheme 밝은 테마용 색상 스키마
 * @param darkScheme 어두운 테마용 색상 스키마
 * @return 동적으로 생성된 Cupertino ColorScheme
 */
@Composable
expect fun dynamicCupertinoColorScheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    lightScheme: ColorScheme,
    darkScheme: ColorScheme
): zone.ien.hig.theme.ColorScheme

/**
 * 적응형 테마 컴포저블
 * 
 * Material 3과 Cupertino 테마를 모두 지원하는 적응형 테마 컴포저블입니다.
 * 사용자는 타겟 플랫폼과 테마 상태에 따라 적절한 테마를 적용받을 수 있습니다.
 * 
 * @param target 적용할 타겟 테마 (Material or Cupertino)
 * @param useDarkTheme 어두운 테마 사용 여부
 * @param useDynamicColor 동적 색상 사용 여부 (Android용)
 * @param shapes 플랫폼별 모양 설정
 * @param lightScheme 밝은 테마 색상 스키마
 * @param darkScheme 어두운 테마 색상 스키마
 * @param materialTypography Material 테마용 타이포그래피
 * @param cupertinoTypography Cupertino 테마용 타이포그래피
 * @param values CompositionLocal에 제공할 값들
 * @param content 내부 콘텐츠
 */
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
    /**
     * 적응형 테마 컴포저블에서 실제 테마 적용 로직을 정의합니다.
     * 
     * @param target 사용할 타겟 테마 (Material or Cupertino)
     * @param useDarkTheme 어두운 테마 사용 여부
     * @param useDynamicColor 동적 색상 사용 여부 (Android용)
     * @param shapes 플랫폼별 모양
     * @param lightScheme 밝은 테마 색상 스키마
     * @param darkScheme 어두운 테마 색상 스키마
     * @param materialTypography Material 테마용 타이포그래피
     * @param cupertinoTypography Cupertino 테마용 타이포그래피
     * @param values CompositionLocal에 제공할 값들
     * @param content 내부 콘텐츠
     */
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