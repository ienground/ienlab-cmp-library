package zone.ien.utils.adaptive.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.hig.adaptive.AdaptiveCheckbox as HigAdaptiveCheckbox
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.interactive.IenCircleCheckbox
import zone.ien.utils.ui.interactive.IenDotCheckbox
import zone.ien.utils.ui.interactive.IenLineCheckbox

/**
 * Material 분기에서 [IenCircleCheckbox]를 사용하는 적응형 체크박스 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    AdaptiveWidget(
        material = {
            IenCircleCheckbox(
                modifier = modifier,
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                size = size,
                label = label,
            )
        },
        cupertino = {
            HigAdaptiveCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
            )
        },
    )
}

/**
 * Material 분기에서 [IenDotCheckbox]를 사용하는 적응형 점 체크박스 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveDotCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 8.dp,
    label: String? = null,
) {
    AdaptiveWidget(
        material = {
            IenDotCheckbox(
                modifier = modifier,
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                size = size,
                label = label,
            )
        },
        cupertino = {
            HigAdaptiveCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
            )
        },
    )
}

/**
 * Material 분기에서 [IenLineCheckbox]를 사용하는 적응형 라인 체크박스 컴포저블.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveLineCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    size: Dp = 24.dp,
    label: String? = null,
) {
    AdaptiveWidget(
        material = {
            IenLineCheckbox(
                modifier = modifier,
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                size = size,
                label = label,
            )
        },
        cupertino = {
            HigAdaptiveCheckbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
            )
        },
    )
}
