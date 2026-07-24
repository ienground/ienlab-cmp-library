package zone.ien.utils.ui.foundation

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * IenTheme에서 사용하는 타이포그래피(글꼴 스타일) 정보를 정의하는 데이터 클래스입니다.
 *
 * @property display 가장 크고 강조되는 헤드라인 텍스트 스타일.
 * @property title1 레벨 1 제목 텍스트 스타일.
 * @property title2 레벨 2 제목 텍스트 스타일.
 * @property title3 레벨 3 제목 텍스트 스타일.
 * @property body1 기본 본문 텍스트 스타일 (큰 글씨용).
 * @property body2 보조 본문 텍스트 스타일 (작은 글씨용).
 * @property label1 버튼, 칩 등 레이블에 사용하는 기본 텍스트 스타일.
 * @property label2 더 작거나 보조적인 레이블에 사용하는 텍스트 스타일.
 * @property caption 설명, 캡션 등 가장 작은 크기의 텍스트 스타일.
 */
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

/**
 * IenTheme에서 사용하는 여백(Spacing) 및 패딩 단위를 정의하는 데이터 클래스입니다.
 *
 * @property none 여백 없음 (0.dp)
 * @property xxxs 극소 여백 (2.dp)
 * @property xxs 매우 작은 여백 (4.dp)
 * @property xs 작은 여백 (8.dp)
 * @property sm 조금 작은 여백 (12.dp)
 * @property md 중간 크기 여백 (16.dp)
 * @property lg 조금 큰 여백 (20.dp)
 * @property xl 큰 여백 (24.dp)
 * @property xxl 매우 큰 여백 (32.dp)
 * @property xxxl 극대 여백 (40.dp)
 */
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

/**
 * IenTheme에서 사용하는 모서리 둥글기(Radius) 값을 정의하는 데이터 클래스입니다.
 *
 * @property none 둥글기 없음 (직각, 0.dp)
 * @property xs 매우 작은 둥글기 (4.dp)
 * @property sm 작은 둥글기 (8.dp)
 * @property default 기본 둥글기 (12.dp)
 * @property md 중간 크기 둥글기 (12.dp)
 * @property lg 큰 둥글기 (16.dp)
 * @property xl 매우 큰 둥글기 (24.dp)
 * @property full 완전한 원형 형태를 위한 둥글기 (999.dp)
 */
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

/**
 * IenTheme에서 사용하는 선 두께(Stroke Width) 값을 정의하는 데이터 클래스입니다.
 *
 * @property hairline 가장 얇은 실선 두께 (0.5.dp)
 * @property thin 얇은 선 두께 (1.dp)
 * @property medium 중간 두께 선 (1.5.dp)
 * @property thick 두꺼운 선 두께 (2.dp)
 */
@Immutable
data class IenStroke(
    val hairline: Dp = 0.5.dp,
    val thin: Dp = 1.dp,
    val medium: Dp = 1.5.dp,
    val thick: Dp = 2.dp,
)

/**
 * IenTheme에서 사용하는 그림자 깊이/고도(Elevation) 값을 정의하는 데이터 클래스입니다.
 *
 * @property none 깊이 없음 (바닥면에 밀착, 0.dp)
 * @property raised 살짝 떠 있는 고도 (카드 등, 4.dp)
 * @property floating 둥둥 떠 있는 고도 (플로팅 액션 버튼 등, 12.dp)
 * @property overlay 최상위에 떠 있는 고도 (다이얼로그, 바텀 시트 등, 24.dp)
 */
@Immutable
data class IenElevation(
    val none: Dp = 0.dp,
    val raised: Dp = 4.dp,
    val floating: Dp = 12.dp,
    val overlay: Dp = 24.dp,
)

/**
 * IenTheme에서 사용하는 아이콘 크기 규격을 정의하는 데이터 클래스입니다.
 *
 * @property xs 가장 작은 아이콘 크기 (12.dp)
 * @property sm 작은 아이콘 크기 (16.dp)
 * @property md 보통 아이콘 크기 (20.dp)
 * @property lg 큰 아이콘 크기 (24.dp)
 * @property xl 매우 큰 아이콘 크기 (32.dp)
 */
@Immutable
data class IenIconSize(
    val xs: Dp = 12.dp,
    val sm: Dp = 16.dp,
    val md: Dp = 20.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

/**
 * IenTheme에서 사용하는 애니메이션 효과의 시간(Duration) 및 감속 곡선(Easing) 정보를 정의하는 데이터 클래스입니다.
 *
 * @property instantMillis 즉각적인 전환 속도 (80ms)
 * @property fastMillis 빠른 속도 전환 (160ms)
 * @property normalMillis 일반적인 속도 전환 (240ms)
 * @property slowMillis 느린 속도 전환 (360ms)
 * @property standardEasing 기본 가속도 곡선 (Easing)
 */
@Immutable
data class IenMotion(
    val instantMillis: Int = 80,
    val fastMillis: Int = 160,
    val normalMillis: Int = 240,
    val slowMillis: Int = 360,
    val standardEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f),
)

/**
 * IenTheme에서 컴포넌트의 상호작용 상태(State)에 따른 불투명도(Alpha) 및 크기 제한 등을 정의하는 데이터 클래스입니다.
 *
 * @property disabledAlpha 비활성화(Disabled) 상태일 때 적용하는 투명도 값.
 * @property pressedAlpha 눌림(Pressed) 상태일 때 오버레이 등에 적용하는 투명도 값.
 * @property focusedAlpha 포커스(Focused) 상태일 때 오버레이 등에 적용하는 투명도 값.
 * @property selectedAlpha 선택(Selected) 상태일 때 오버레이 등에 적용하는 투명도 값.
 * @property minimumTouchTarget 접근성을 위한 최소 터치 영역 크기 (44.dp)
 */
@Immutable
data class IenStateTokens(
    val disabledAlpha: Float = 0.38f,
    val pressedAlpha: Float = 0.10f,
    val focusedAlpha: Float = 0.12f,
    val selectedAlpha: Float = 0.14f,
    val minimumTouchTarget: Dp = 44.dp,
)

/**
 * IenTheme의 모든 토큰 정보(색상, 타이포그래피, 간격 등)를 모아놓은 통합 데이터 클래스입니다.
 *
 * @property lightColors 라이트 테마 색상 스키마.
 * @property darkColors 다크 테마 색상 스키마. 지정하지 않으면 [lightColors]와 같은 값을 사용합니다.
 * @property typography 테마 글꼴 규격
 * @property spacing 테마 간격/여백 규격
 * @property radius 테마 모서리 둥글기 규격
 * @property stroke 테마 선 두께 규격
 * @property elevation 테마 그림자 고도 규격
 * @property icon 테마 아이콘 크기 규격
 * @property motion 테마 모션/애니메이션 규격
 * @property state 테마 상태값 규격
 */
@Immutable
data class IenTokens(
    val lightColors: IenColorScheme = defaultLightIenColorScheme(),
    val darkColors: IenColorScheme = defaultDarkIenColorScheme(),
    val typography: IenTypography = defaultIenTypography(),
    val spacing: IenSpacing = IenSpacing(),
    val radius: IenRadius = IenRadius(),
    val stroke: IenStroke = IenStroke(),
    val elevation: IenElevation = IenElevation(),
    val icon: IenIconSize = IenIconSize(),
    val motion: IenMotion = IenMotion(),
    val state: IenStateTokens = IenStateTokens(),
)

/**
 * CompositionLocal을 통해 하위 컴포저블 트리로 [IenTokens]를 전달하기 위한 키 객체입니다.
 */
val LocalIenTokens = staticCompositionLocalOf { defaultIenTokens() }

/**
 * 현재 IenTheme가 다크 색상 스키마를 사용 중인지 전달하는 CompositionLocal입니다.
 */
val LocalIenDarkTheme = staticCompositionLocalOf { false }

/**
 * 앱 전반의 테마 정보에 쉽게 접근할 수 있도록 해주는 싱글톤 테마 객체입니다.
 *
 * 하위 컴포저블에서 `IenTheme.colors`와 같이 사용하여 테마 스타일을 동적으로 적용받을 수 있습니다.
 */
object IenTheme {
    /** 현재 테마의 색상 스키마 */
    val colors: IenColorScheme
        @Composable get() {
            val tokens = LocalIenTokens.current
            return if (LocalIenDarkTheme.current) tokens.darkColors else tokens.lightColors
        }

    /** 현재 테마의 타이포그래피 스타일 */
    val typography: IenTypography
        @Composable get() = LocalIenTokens.current.typography

    /** 현재 테마의 여백 규격 */
    val spacing: IenSpacing
        @Composable get() = LocalIenTokens.current.spacing

    /** 현재 테마의 모서리 둥글기 규격 */
    val radius: IenRadius
        @Composable get() = LocalIenTokens.current.radius

    /** 현재 테마의 선 두께 규격 */
    val stroke: IenStroke
        @Composable get() = LocalIenTokens.current.stroke

    /** 현재 테마의 그림자 고도 규격 */
    val elevation: IenElevation
        @Composable get() = LocalIenTokens.current.elevation

    /** 현재 테마의 아이콘 크기 규격 */
    val icon: IenIconSize
        @Composable get() = LocalIenTokens.current.icon

    /** 현재 테마의 모션 규격 */
    val motion: IenMotion
        @Composable get() = LocalIenTokens.current.motion

    /** 현재 테마의 상태별 토큰 규격 */
    val state: IenStateTokens
        @Composable get() = LocalIenTokens.current.state
}

/**
 * 앱에 Ien 디자인 시스템 테마를 주입하는 최상위 컴포저블 테마 프로바이더입니다.
 *
 * @param darkTheme 시스템 설정이나 사용자 기호에 따라 다크 모드를 활성화할지 여부. 기본값은 시스템 설정을 따릅니다.
 * @param tokens 테마 커스텀 토큰 정보. null인 경우 [darkTheme] 인자에 매칭되는 기본 테마가 설정됩니다.
 * @param content 이 테마 설정을 적용받을 하위 컴포저블 콘텐츠.
 */
@Composable
fun IenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    tokens: IenTokens? = null,
    content: @Composable () -> Unit,
) {
    val resolvedTokens = tokens ?: LocalIenTokens.current

    androidx.compose.runtime.CompositionLocalProvider(
        LocalIenTokens provides resolvedTokens,
        LocalIenDarkTheme provides darkTheme,
        content = content,
    )
}

/**
 * 라이트/다크 색상 스키마를 모두 포함한 기본 Ien 토큰을 생성하고 반환합니다.
 */
fun defaultIenTokens() = IenTokens(
    lightColors = defaultLightIenColorScheme(),
    darkColors = defaultDarkIenColorScheme(),
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
