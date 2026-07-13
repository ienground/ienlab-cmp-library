package zone.ien.utils.ui.feedback

import androidx.annotation.FloatRange
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.LoadingIndicatorDefaults
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.WavyProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import zone.ien.utils.ui.foundation.IenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    strokeWidth: Dp = 4.dp,
    trackColor: Color = IenTheme.colors.brandWeak,
    strokeCap: StrokeCap = StrokeCap.Round,
    gapSize: Dp = 4.dp,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenCircularWavyProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = IenTheme.colors.brandWeak,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = 4.dp,
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

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenCircularWavyProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = IenTheme.colors.brandWeak,
    stroke: Stroke? = null,
    trackStroke: Stroke? = null,
    gapSize: Dp = 4.dp,
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

@Composable
fun IenLinearProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = IenTheme.colors.brand,
    trackColor: Color = IenTheme.colors.brandWeak,
    strokeCap: StrokeCap = StrokeCap.Round,
) {
    LinearProgressIndicator(
        modifier = modifier,
        color = color,
        trackColor = trackColor,
        strokeCap = strokeCap,
    )
}

