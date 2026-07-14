package zone.ien.utils.ui.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenTheme

/**
 * LocalDialogProviderDefault은 다이얼로그의 기본 제공 값을 정의하는 내부 객체입니다.
 * 기본 테마 색상과 모양을 기반으로 기본 다이얼로그 스타일을 제공합니다.
 */
internal object LocalDialogProviderDefault {
    /**
     * 기본 다이얼로그 모양을 반환합니다.
     */
    val Shape @Composable get() = ContinuousRoundedRectangle(IenTheme.radius.xl)
    
    /**
     * 기본 다이얼로그 배경 색상을 반환합니다.
     */
    val BackgroundColor @Composable get() = IenTheme.colors.surfaceRaised
    
    /**
     * 기본 다이얼로그 내용 색상을 반환합니다.
     */
    val ContentColor @Composable get() = IenTheme.colors.textPrimary
}

/**
 * LocalDialogShape는 다이얼로그 모양을 제공하는 CompositionLocal입니다.
 */
val LocalDialogShape: ProvidableCompositionLocal<Shape?> = staticCompositionLocalOf { null }

/**
 * LocalDialogBorder는 다이얼로그 테두리를 제공하는 CompositionLocal입니다.
 */
val LocalDialogBorder: ProvidableCompositionLocal<BorderStroke?> = staticCompositionLocalOf { null }

/**
 * LocalDialogBackgroundColor은 다이얼로그 배경 색상을 제공하는 CompositionLocal입니다.
 */
val LocalDialogBackgroundColor: ProvidableCompositionLocal<Color?> = staticCompositionLocalOf { null }

/**
 * LocalDialogContentColor는 다이얼로그 내용 색상을 제공하는 CompositionLocal입니다.
 */
val LocalDialogContentColor: ProvidableCompositionLocal<Color?> = staticCompositionLocalOf { null }
