package zone.ien.utils.ui.feedback

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.toneColor
import zone.ien.utils.ui.interactive.toneWeakColor

/** 진행 표시기의 원형·선형 배치를 지정합니다. */
enum class IenProgressIndicatorVariant { Circular, Linear }

/** 진행선의 평평한 모양·물결 모양을 지정합니다. */
enum class IenProgressIndicatorShape { Flat, Wavy }

/**
 * 원형·선형 배치와 평평한·물결 모양을 조합하는 진행 표시기입니다.
 *
 * @param modifier 표시기 루트에 적용할 수정자
 * @param variant 원형 또는 선형 배치
 * @param shape 진행선 모양
 * @param tone 진행선과 미진행 트랙에 함께 적용할 의미론적 색상
 * @param progress 0~1 진행률을 반환하는 함수. 생략하면 불확정 애니메이션을 표시합니다.
 * @param color 진행선 색상. 기본값은 [tone]에 해당하는 테마 색상입니다.
 * @param trackColor 미진행 트랙 색상. 기본값은 [tone]에 해당하는 옅은 테마 색상입니다.
 * @param strokeWidth 진행선과 트랙의 두께
 * @param gapSize 진행선과 트랙 사이 간격
 * @param amplitude 물결 진폭 비율(0~1). 확정 진행률에서는 양 끝으로 갈수록 진폭이 줄어듭니다.
 * @param wavelength 물결 한 파장의 길이
 * @param waveSpeed 초당 물결 이동 거리
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenProgressIndicator(
    modifier: Modifier = Modifier,
    variant: IenProgressIndicatorVariant = IenProgressIndicatorVariant.Circular,
    shape: IenProgressIndicatorShape = IenProgressIndicatorShape.Flat,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    progress: (() -> Float)? = null,
    color: Color = toneColor(tone),
    trackColor: Color = toneWeakColor(tone),
    strokeWidth: Dp = 4.dp,
    gapSize: Dp = IenTheme.spacing.xxs,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = 1f,
    wavelength: Dp = 40.dp,
    waveSpeed: Dp = wavelength,
) {
    val safeProgress = progress?.let { value -> { value().coerceIn(0f, 1f) } }
    if (shape == IenProgressIndicatorShape.Wavy) {
        val stroke = with(LocalDensity.current) {
            Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
        }
        val safeAmplitude = amplitude.coerceIn(0f, 1f)
        val indicatorAmplitude: (Float) -> Float = {
            WavyProgressIndicatorDefaults.indicatorAmplitude(it) * safeAmplitude
        }
        if (variant == IenProgressIndicatorVariant.Circular) {
            if (safeProgress != null) {
                CircularWavyProgressIndicator(
                    progress = safeProgress,
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    stroke = stroke,
                    trackStroke = stroke,
                    gapSize = gapSize,
                    amplitude = indicatorAmplitude,
                    wavelength = wavelength,
                    waveSpeed = waveSpeed,
                )
            } else {
                CircularWavyProgressIndicator(
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    stroke = stroke,
                    trackStroke = stroke,
                    gapSize = gapSize,
                    amplitude = safeAmplitude,
                    wavelength = wavelength,
                    waveSpeed = waveSpeed,
                )
            }
        } else {
            if (safeProgress != null) {
                LinearWavyProgressIndicator(
                    progress = safeProgress,
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    stroke = stroke,
                    trackStroke = stroke,
                    gapSize = gapSize,
                    amplitude = indicatorAmplitude,
                    wavelength = wavelength,
                    waveSpeed = waveSpeed,
                )
            } else {
                LinearWavyProgressIndicator(
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    stroke = stroke,
                    trackStroke = stroke,
                    gapSize = gapSize,
                    amplitude = safeAmplitude,
                    wavelength = wavelength,
                    waveSpeed = waveSpeed,
                )
            }
        }
    } else {
        if (variant == IenProgressIndicatorVariant.Circular) {
            if (safeProgress != null) {
                CircularProgressIndicator(
                    progress = safeProgress,
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    strokeWidth = strokeWidth,
                    strokeCap = StrokeCap.Round,
                    gapSize = gapSize,
                )
            } else {
                CircularProgressIndicator(
                    modifier = modifier,
                    color = color,
                    trackColor = trackColor,
                    strokeWidth = strokeWidth,
                    strokeCap = StrokeCap.Round,
                    gapSize = gapSize,
                )
            }
        } else {
            if (safeProgress != null) {
                LinearProgressIndicator(
                    progress = safeProgress,
                    modifier = modifier.height(strokeWidth),
                    color = color,
                    trackColor = trackColor,
                    strokeCap = StrokeCap.Round,
                    gapSize = gapSize,
                )
            } else {
                LinearProgressIndicator(
                    modifier = modifier.height(strokeWidth),
                    color = color,
                    trackColor = trackColor,
                    strokeCap = StrokeCap.Round,
                    gapSize = gapSize,
                )
            }
        }
    }
}

@Composable
private fun progressTrackColor(color: Color): Color {
    for (tone in IenSemanticTone.entries) {
        if (color == toneColor(tone)) return toneWeakColor(tone)
    }
    return color.copy(alpha = color.alpha * 0.16f)
}

/**
 * Material3 스타일의 둥근 모양 회전식 프로그레스 인디케이터 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param color 진행률 영역에 칠할 주 색상
 * @param strokeWidth 선의 굵기 ([Dp])
 * @param trackColor 진행선 뒷배경 트랙 색상. 기본값은 [color]에 대응하는 옅은 테마 색상입니다.
 * @param strokeCap 선 끝부분의 라운딩 처리 여부 ([StrokeCap])
 * @param gapSize 선 끝부분 사이의 간격 수치 ([Dp])
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = progressTrackColor(color),
    strokeCap: StrokeCap = StrokeCap.Round,
    gapSize: Dp = IenTheme.spacing.xxs,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
        strokeCap = strokeCap,
        gapSize = gapSize,
    )
}

/**
 * 물결 모양 애니메이션이 가미된 원형 프로그레스 인디케이터 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param color 물결 진행률 부분의 선 색상
 * @param trackColor 진행 트랙 색상. 기본값은 [color]에 대응하는 옅은 테마 색상입니다.
 * @param stroke 인디케이터에 적용할 그리기 스타일 스펙 ([Stroke])
 * @param trackStroke 트랙 영역에 적용할 그리기 스타일 스펙 ([Stroke])
 * @param gapSize 물결 마디 사이의 틈 크기 ([Dp])
 * @param amplitude 물결의 진폭 높이 비율 (0.0 ~ 1.0)
 * @param wavelength 물결 한 파장의 너비 수치 ([Dp])
 * @param waveSpeed 물결이 회전하는 움직임 속도 ([Dp])
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenCircularWavyProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = progressTrackColor(color),
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = IenTheme.spacing.xxs,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = 1f,
    wavelength: Dp = 40.dp,
    waveSpeed: Dp = wavelength,
) {
    CircularWavyProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        stroke = stroke ?: WavyProgressIndicatorDefaults.circularIndicatorStroke,
        trackStroke = trackStroke ?: WavyProgressIndicatorDefaults.circularTrackStroke,
        gapSize = gapSize,
        amplitude = amplitude,
        wavelength = wavelength,
        waveSpeed = waveSpeed,
    )
}

/**
 * 지정된 진행률 람다([progress])에 맞춰 물결 모양의 원형 인디케이터를 렌더링하는 컴포저블입니다.
 *
 * @param progress 0.0부터 1.0 사이의 진행률 상태를 제공하는 getter 람다 식
 * @param modifier 적용할 [Modifier]
 * @param color 물결 진행률 부분의 선 색상
 * @param trackColor 진행 트랙 색상. 기본값은 [color]에 대응하는 옅은 테마 색상입니다.
 * @param stroke 인디케이터에 적용할 그리기 스타일 스펙 ([Stroke])
 * @param trackStroke 트랙 영역에 적용할 그리기 스타일 스펙 ([Stroke])
 * @param gapSize 물결 마디 사이의 틈 크기 ([Dp])
 * @param amplitude 진행률 상태값에 맞춰 실시간 진폭 비율을 계산하는 함수
 * @param wavelength 물결 한 파장의 너비 수치 ([Dp])
 * @param waveSpeed 물결이 회전하는 움직임 속도 ([Dp])
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenCircularWavyProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = progressTrackColor(color),
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = IenTheme.spacing.xxs,
    amplitude: ((progress: Float) -> Float)? = null,
    wavelength: Dp = 40.dp,
    waveSpeed: Dp = wavelength,
) {
    CircularWavyProgressIndicator(
        progress = progress,
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        stroke = stroke ?: WavyProgressIndicatorDefaults.circularIndicatorStroke,
        trackStroke = trackStroke ?: WavyProgressIndicatorDefaults.circularTrackStroke,
        gapSize = gapSize,
        amplitude = amplitude ?: WavyProgressIndicatorDefaults.indicatorAmplitude,
        wavelength = wavelength,
        waveSpeed = waveSpeed,
    )
}

/**
 * 커스텀 다각형 애니메이션을 순차적으로 수행하며 로딩 상태를 연출해 주는 로딩 인디케이터 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param color 인디케이터에 칠할 주 색상
 * @param polygons 애니메이션 변환 단계에 사용할 둥근 다각형([RoundedPolygon]) 목록
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenLoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    polygons: List<RoundedPolygon>? = null,
) {
    LoadingIndicator(
        modifier = modifier,
        color = color,
        polygons = polygons ?: LoadingIndicatorDefaults.IndeterminateIndicatorPolygons,
    )
}

/**
 * 가로로 뻗어 나가는 선형 프로그레스 인디케이터 컴포저블입니다.
 *
 * @param modifier 적용할 [Modifier]
 * @param color 진행선 채우기 색상
 * @param trackColor 뒷배경 트랙 색상. 기본값은 [color]에 대응하는 옅은 테마 색상입니다.
 * @param strokeCap 선 끝단 모서리 스타일 ([StrokeCap])
 */
@Composable
fun IenLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = progressTrackColor(color),
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    LinearProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap,
    )
}

