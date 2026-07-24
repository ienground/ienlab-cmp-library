package zone.ien.utils.adaptive.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.adaptive.AdaptiveSlider as HigAdaptiveSlider
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.interactive.IenSlider

/**
 * Material 분기에서 [IenSlider]를 사용하는 적응형 슬라이더 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    valueLabel: String? = null,
) {
    AdaptiveWidget(
        material = {
            IenSlider(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                valueRange = valueRange,
                steps = steps,
                enabled = enabled,
                label = label,
                valueLabel = valueLabel,
            )
        },
        cupertino = {
            HigAdaptiveSlider(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                enabled = enabled,
                valueRange = valueRange,
                steps = steps,
            )
        },
    )
}
