package zone.ien.utils.adaptive.view

import androidx.annotation.FloatRange
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults
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

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveCircularProgressIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, MaterialCircularProgressIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { CircularProgressIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            CircularProgressIndicator(
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

@OptIn(
    ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun AdaptiveCircularWavyProgressIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, MaterialWavyCircularProgressIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { WavyProgressIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            CircularWavyProgressIndicator(
                modifier = it.modifier,
                color = it.color,
                trackColor = it.trackColor,
                stroke = it.stroke ?: WavyProgressIndicatorDefaults.circularIndicatorStroke,
                trackStroke = it.trackStroke ?: WavyProgressIndicatorDefaults.circularTrackStroke,
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

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3ExpressiveApi::class,
    ExperimentalCupertinoApi::class
)
@Composable
fun AdaptiveLoadingIndicator(
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, MaterialLoadingIndicatorAdaptation>.() -> Unit = {}
) {
    AdaptiveWidget(
        adaptation = remember { LoadingIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            LoadingIndicator(
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

@OptIn(
    ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun AdaptiveCircularWavyProgressIndicator(
    progress: () -> Float,
    adaptation: AdaptationScope<CupertinoCircularProgressIndicatorAdaptation, MaterialWavyCircularDeterminedProgressIndicatorAdaptation>.() -> Unit = {}
) {
    val currentProgress by animateFloatAsState(
        targetValue = progress.invoke().let { if (it > 1f) 1f else if (it < 0f) 0f else it }
    )

    AdaptiveWidget(
        adaptation = remember { WavyProgressDeterminedIndicatorAdaptation() },
        adaptationScope = adaptation,
        material = {
            CircularWavyProgressIndicator(
                progress = { currentProgress },
                modifier = it.modifier,
                color = it.color,
                trackColor = it.trackColor,
                stroke = it.stroke ?: WavyProgressIndicatorDefaults.circularIndicatorStroke,
                trackStroke = it.trackStroke ?: WavyProgressIndicatorDefaults.circularTrackStroke,
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

class MaterialCircularProgressIndicatorAdaptation @OptIn(ExperimentalMaterial3Api::class) constructor(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = Color.Unspecified,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap,
    gapSize: Dp = ProgressIndicatorDefaults.CircularIndicatorTrackGapSize,
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var strokeWidth: Dp by mutableStateOf(strokeWidth)
    var trackColor: Color by mutableStateOf(trackColor)
    var strokeCap: StrokeCap by mutableStateOf(strokeCap)
    var gapSize: Dp by mutableStateOf(gapSize)
}

class MaterialWavyCircularProgressIndicatorAdaptation @OptIn(ExperimentalMaterial3ExpressiveApi::class) constructor(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = WavyProgressIndicatorDefaults.CircularIndicatorTrackGapSize,
    @FloatRange(from = 0.0, to = 1.0) amplitude: Float = 1f,
    wavelength: Dp = WavyProgressIndicatorDefaults.CircularWavelength,
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

class MaterialWavyCircularDeterminedProgressIndicatorAdaptation @OptIn(ExperimentalMaterial3ExpressiveApi::class) constructor(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    trackColor: Color = Color.Unspecified,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = WavyProgressIndicatorDefaults.CircularIndicatorTrackGapSize,
    amplitude: (progress: Float) -> Float = WavyProgressIndicatorDefaults.indicatorAmplitude,
    wavelength: Dp = WavyProgressIndicatorDefaults.CircularWavelength,
    waveSpeed: Dp = wavelength
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var trackColor: Color by mutableStateOf(trackColor)
    var stroke: Stroke? by mutableStateOf(stroke)
    var trackStroke: Stroke? by mutableStateOf(trackStroke)
    var gapSize: Dp by mutableStateOf(gapSize)
    var amplitude: (progress: Float) -> Float by mutableStateOf(amplitude)
    var wavelength: Dp by mutableStateOf(wavelength)
    var waveSpeed: Dp by mutableStateOf(waveSpeed)
}

class MaterialLoadingIndicatorAdaptation @OptIn(ExperimentalMaterial3ExpressiveApi::class) constructor(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    polygons: List<RoundedPolygon> = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons,
) {
    var modifier: Modifier by mutableStateOf(modifier)
    var color: Color by mutableStateOf(color)
    var polygons: List<RoundedPolygon> by mutableStateOf(polygons)
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

private class CircularProgressIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<MaterialCircularProgressIndicatorAdaptation>() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun rememberMaterialAdaptation(): MaterialCircularProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = ProgressIndicatorDefaults.circularColor
        val strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth
        val trackColor: Color = ProgressIndicatorDefaults.circularDeterminateTrackColor
        val strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
        val gapSize: Dp = ProgressIndicatorDefaults.CircularIndicatorTrackGapSize

        return remember(modifier, color, strokeWidth, trackColor, strokeCap, gapSize) {
            MaterialCircularProgressIndicatorAdaptation(
                modifier, color, strokeWidth, trackColor, strokeCap, gapSize
            )
        }
    }
}

private class WavyProgressIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<MaterialWavyCircularProgressIndicatorAdaptation>() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun rememberMaterialAdaptation(): MaterialWavyCircularProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = WavyProgressIndicatorDefaults.indicatorColor
        val trackColor: Color = WavyProgressIndicatorDefaults.trackColor
        val stroke: Stroke = WavyProgressIndicatorDefaults.circularIndicatorStroke
        val trackStroke: Stroke = WavyProgressIndicatorDefaults.circularTrackStroke
        val gapSize: Dp = WavyProgressIndicatorDefaults.CircularIndicatorTrackGapSize
        val amplitude: Float = 1f
        val wavelength: Dp = WavyProgressIndicatorDefaults.CircularWavelength
        val waveSpeed: Dp = wavelength

        return remember(modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed) {
            MaterialWavyCircularProgressIndicatorAdaptation(
                modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed
            )
        }
    }
}

private class WavyProgressDeterminedIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<MaterialWavyCircularDeterminedProgressIndicatorAdaptation>() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun rememberMaterialAdaptation(): MaterialWavyCircularDeterminedProgressIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = WavyProgressIndicatorDefaults.indicatorColor
        val trackColor: Color = WavyProgressIndicatorDefaults.trackColor
        val stroke: Stroke = WavyProgressIndicatorDefaults.circularIndicatorStroke
        val trackStroke: Stroke = WavyProgressIndicatorDefaults.circularTrackStroke
        val gapSize: Dp = WavyProgressIndicatorDefaults.CircularIndicatorTrackGapSize
        val amplitude: (progress: Float) -> Float = WavyProgressIndicatorDefaults.indicatorAmplitude
        val wavelength: Dp = WavyProgressIndicatorDefaults.CircularWavelength
        val waveSpeed: Dp = wavelength

        return remember(modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed) {
            MaterialWavyCircularDeterminedProgressIndicatorAdaptation(
                modifier, color, trackColor, stroke, trackStroke, gapSize, amplitude, wavelength, waveSpeed
            )
        }
    }
}

private class LoadingIndicatorAdaptation: BaseCircularProgressIndicatorAdaptation<MaterialLoadingIndicatorAdaptation>() {
    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    override fun rememberMaterialAdaptation(): MaterialLoadingIndicatorAdaptation {
        val modifier: Modifier = Modifier
        val color: Color = LoadingIndicatorDefaults.indicatorColor
        val polygons: List<RoundedPolygon> = LoadingIndicatorDefaults.IndeterminateIndicatorPolygons

        return remember(modifier, color, polygons) {
            MaterialLoadingIndicatorAdaptation(
                modifier, color, polygons
            )
        }
    }
}