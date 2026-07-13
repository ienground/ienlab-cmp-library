package zone.ien.utils.adaptive.view

import androidx.annotation.FloatRange
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import zone.ien.hig.CupertinoActivityIndicator
import zone.ien.hig.CupertinoActivityIndicatorDefaults
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.Adaptation
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.Gray
import zone.ien.utils.ui.feedback.IenCircularProgressIndicator
import zone.ien.utils.ui.feedback.IenCircularWavyProgressIndicator
import zone.ien.utils.ui.feedback.IenLoadingIndicator
import zone.ien.utils.ui.foundation.IenTheme

/**
 * 적응형 원형 진행 표시기 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 원형 진행 표시기를 제공합니다.
 * 
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveCircularProgressIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, IenCircularProgressIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { CircularProgressIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenCircularProgressIndicator(
                modifier = it.modifier,
                color = it.color,
                strokeWidth = it.strokeWidth,
                trackColor = it.trackColor,
                strokeCap = it.strokeCap,
                gapSize = it.gapSize
            )
        },
        cupertino = {
            CupertinoActivityIndicator(
                modifier = it.modifier,
                size = it.size,
                color = it.color,
                count = it.count,
                innerRadius = it.innerRadius,
                strokeWidth = it.strokeWidth,
                animationSpec = it.animationSpec,
                minAlpha = it.minAlpha
            )
        },
    )
}

/**
 * 적응형 파형 원형 진행 표시기 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 파형 원형 진행 표시기를 제공합니다.
 * 
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(
    ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveCircularWavyProgressIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, IenWavyCircularProgressIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { WavyProgressIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenCircularWavyProgressIndicator(
                modifier = it.modifier,
                color = it.color,
                trackColor = it.trackColor,
                stroke = it.stroke,
                trackStroke = it.trackStroke,
                gapSize = it.gapSize,
                amplitude = it.amplitude,
                wavelength = it.wavelength,
                waveSpeed = it.waveSpeed
            )
        },
        cupertino = {
            CupertinoActivityIndicator(
                modifier = it.modifier,
                size = it.size,
                color = it.color,
                count = it.count,
                innerRadius = it.innerRadius,
                strokeWidth = it.strokeWidth,
                animationSpec = it.animationSpec,
                minAlpha = it.minAlpha
            )
        },
    )
}

/**
 * 적응형 로딩 표시기 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 로딩 표시기를 제공합니다.
 * 
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveLoadingIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, IenLoadingIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { LoadingIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenLoadingIndicator(
                modifier = it.modifier,
                color = it.color,
                polygons = it.polygons
            )
        },
        cupertino = {
            CupertinoActivityIndicator(
                modifier = it.modifier,
                size = it.size,
                color = it.color,
                count = it.count,
                innerRadius = it.innerRadius,
                strokeWidth = it.strokeWidth,
                animationSpec = it.animationSpec,
                minAlpha = it.minAlpha
            )
        },
    )
}

/**
 * 적응형 파형 원형 진행 표시기 (확정적)
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 파형 원형 진행 표시기를 제공합니다.
 * 
 * @param progress 진행률을 반환하는 함수
 * @param adaptation 플랫폼별 적응형 설정을 위한 블록
 */
@OptIn(
    ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveCircularWavyProgressIndicator(
    progress: () -> Float,
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, IenWavyCircularDeterminedProgressIndicatorAdaptation>.() -> Unit = {}
) {
    val currentProgress by animateFloatAsState(
        targetValue = progress.invoke().let { if (it > 1f) 1f else if (it < 0f) 0f else it }
    )

    AdaptiveWidget(
        adaptation = remember { WavyProgressDeterminedIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            IenCircularWavyProgressIndicator(
                progress = { currentProgress },
                modifier = it.modifier,
                color = it.color,
                trackColor = it.trackColor,
                stroke = it.stroke,
                trackStroke = it.trackStroke,
                gapSize = it.gapSize,
                amplitude = it.amplitude,
                wavelength = it.wavelength,
                waveSpeed = it.waveSpeed
            )
        },
        cupertino = {
            CupertinoActivityIndicator(
                progress = currentProgress,
                modifier = it.modifier,
                size = it.size,
                color = it.color,
                count = it.count,
                innerRadius = it.innerRadius,
                strokeWidth = it.strokeWidth,
                animationSpec = it.animationSpec,
                minAlpha = it.minAlpha
            )
        }
    )
}

class CupertinoCircularProgressIndicatorAdaptation(
    modifier: Modifier = Modifier,
    size: Dp = CupertinoActivityIndicatorDefaults.MinSize,
    color: Color = CupertinoColors.Gray,
    count: Int = CupertinoActivityIndicatorDefaults.PathCount,
    innerRadius: Float = 1 / 3f,
    strokeWidth: Dp = Dp.Unspecified,
    animationSpec: InfiniteRepeatableSpec<Float> =
        infiniteRepeatable(
            animation =
                tween(
                    durationMillis = CupertinoActivityIndicatorDefaults.DurationMillis,
                    easing = LinearEasing,
                ),
            repeatMode = RepeatMode.Restart,
        ),
    minAlpha: Float = CupertinoActivityIndicatorDefaults.MinAlpha,
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var size: Dp by mutableStateOf(size)
    var color: Color by mutableStateOf(color)
    var count: Int by mutableStateOf(count)
    var innerRadius: Float by mutableStateOf(innerRadius)
    var strokeWidth: Dp by mutableStateOf(strokeWidth)
    var animationSpec: InfiniteRepeatableSpec<Float> by mutableStateOf(animationSpec)
    var minAlpha: Float by mutableStateOf(minAlpha)
}

class IenCircularProgressIndicatorAdaptation(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = Color.Unspecified,
    strokeCap: StrokeCap = StrokeCap.Round,
    gapSize: Dp = 4.dp,
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var strokeWidth: Dp by mutableStateOf(strokeWidth)
    var trackColor: Color by mutableStateOf(trackColor)
    var strokeCap: StrokeCap by mutableStateOf(strokeCap)
    var gapSize: Dp by mutableStateOf(gapSize)
}

class IenWavyCircularProgressIndicatorAdaptation(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = 4.dp,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = 1f,
    wavelength: Dp = 40.dp,
    waveSpeed: Dp = wavelength
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var trackColor: Color by mutableStateOf(trackColor)
    var stroke: Stroke? by mutableStateOf(stroke)
    var trackStroke: Stroke? by mutableStateOf(trackStroke)
    var gapSize: Dp by mutableStateOf(gapSize)
    var amplitude: Float by mutableStateOf(amplitude)
    var wavelength: Dp by mutableStateOf(wavelength)
    var waveSpeed: Dp by mutableStateOf(waveSpeed)
}

class IenWavyCircularDeterminedProgressIndicatorAdaptation(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = 4.dp,
    amplitude: ((progress: Float) -> Float)? = null,
    wavelength: Dp = 40.dp,
    waveSpeed: Dp = wavelength
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var trackColor: Color by mutableStateOf(trackColor)
    var stroke: Stroke? by mutableStateOf(stroke)
    var trackStroke: Stroke? by mutableStateOf(trackStroke)
    var gapSize: Dp by mutableStateOf(gapSize)
    var amplitude: ((progress: Float) -> Float)? by mutableStateOf(amplitude)
    var wavelength: Dp by mutableStateOf(wavelength)
    var waveSpeed: Dp by mutableStateOf(waveSpeed)
}

class IenLoadingIndicatorAdaptation(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    polygons: List<RoundedPolygon>? = null,
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var polygons: List<RoundedPolygon>? by mutableStateOf(polygons)
}


@OptIn(ExperimentalAdaptiveApi::class)
@Stable
private abstract class BaseCircularProgressIndicatorAdaptation<T> : Adaptation<CupertinoCircularProgressIndicatorAdaptation, T>() {
    @Composable
    override fun rememberCupertinoAdaptation(): CupertinoCircularProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val size: Dp = CupertinoActivityIndicatorDefaults.MinSize
        val color: Color = CupertinoColors.Gray
        val count: Int = CupertinoActivityIndicatorDefaults.PathCount
        val innerRadius: Float = 1 / 3f
        val strokeWidth: Dp = Dp.Unspecified
        val animationSpec: InfiniteRepeatableSpec<Float> =
        infiniteRepeatable(
            animation =
                tween(
                    durationMillis = CupertinoActivityIndicatorDefaults.DurationMillis,
                    easing = LinearEasing,
                ),
            repeatMode = RepeatMode.Restart,
        )
        val minAlpha: Float = CupertinoActivityIndicatorDefaults.MinAlpha

        return remember(modifier, size, color, count, innerRadius, strokeWidth, animationSpec, minAlpha) {
            CupertinoCircularProgressIndicatorAdaptation(
                modifier, size, color, count, innerRadius, strokeWidth, animationSpec, minAlpha
            )
        }
    }
}

private class CircularProgressIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<IenCircularProgressIndicatorAdaptation>() {
    @Composable
    override fun rememberMaterialAdaptation(): IenCircularProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = IenTheme.colors.brand
        val strokeWidth: Dp = 4.dp
        val trackColor: Color = IenTheme.colors.brandWeak
        val strokeCap: StrokeCap = StrokeCap.Round
        val gapSize: Dp = 4.dp

        return remember(modifier, color, strokeWidth, trackColor, strokeCap, gapSize) {
            IenCircularProgressIndicatorAdaptation(
                modifier, color, strokeWidth, trackColor, strokeCap, gapSize
            )
        }
    }
}

private class WavyProgressIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<IenWavyCircularProgressIndicatorAdaptation>() {
    @Composable
    override fun rememberMaterialAdaptation(): IenWavyCircularProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = IenTheme.colors.brand
        val trackColor: Color = IenTheme.colors.brandWeak
        val stroke: Stroke? = null
        val trackStroke: Stroke? = null
        val gapSize: Dp = 4.dp
        val amplitude: Float = 1f
        val wavelength: Dp = 40.dp
        val waveSpeed: Dp = wavelength

        return remember(modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed) {
            IenWavyCircularProgressIndicatorAdaptation(
                modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed
            )
        }
    }
}

private class WavyProgressDeterminedIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<IenWavyCircularDeterminedProgressIndicatorAdaptation>() {
    @Composable
    override fun rememberMaterialAdaptation(): IenWavyCircularDeterminedProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = IenTheme.colors.brand
        val trackColor: Color = IenTheme.colors.brandWeak
        val stroke: Stroke? = null
        val trackStroke: Stroke? = null
        val gapSize: Dp = 4.dp
        val amplitude: ((progress: Float) -> Float)? = null
        val wavelength: Dp = 40.dp
        val waveSpeed: Dp = wavelength

        return remember(modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed) {
            IenWavyCircularDeterminedProgressIndicatorAdaptation(
                modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed
            )
        }
    }
}

private class LoadingIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<IenLoadingIndicatorAdaptation>() {
    @Composable
    override fun rememberMaterialAdaptation(): IenLoadingIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = IenTheme.colors.brand
        val polygons: List<RoundedPolygon>? = null

        return remember(modifier, color, polygons) {
            IenLoadingIndicatorAdaptation(
                modifier, color, polygons
            )
        }
    }
}
