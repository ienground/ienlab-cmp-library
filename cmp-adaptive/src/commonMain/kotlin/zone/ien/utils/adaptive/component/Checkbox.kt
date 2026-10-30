package zone.ien.utils.adaptive.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import zone.ien.hig.adaptive.AdaptiveCheckbox as HigAdaptiveCheckbox
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.ui.foundation.IenTheme
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
            CupertinoCheckboxWithLabel(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                label = label,
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
            CupertinoCheckboxWithLabel(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                label = label,
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
            CupertinoCheckboxWithLabel(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                label = label,
            )
        },
    )
}

@Composable
private fun CupertinoCheckboxWithLabel(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier,
    enabled: Boolean,
    label: String?,
) {
    if (label == null) {
        HigAdaptiveCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            enabled = enabled,
        )
    } else {
        val labelledModifier = if (onCheckedChange != null) {
            Modifier.toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Checkbox,
                onValueChange = onCheckedChange,
            )
        } else {
            Modifier.semantics(mergeDescendants = true) {
                role = Role.Checkbox
                toggleableState = ToggleableState(checked)
                if (!enabled) disabled()
            }
        }

        Row(
            modifier = modifier.then(labelledModifier),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HigAdaptiveCheckbox(
                checked = checked,
                onCheckedChange = null,
                modifier = Modifier,
                enabled = enabled,
            )
            Text(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}
