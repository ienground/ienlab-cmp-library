package zone.ien.utils.ui.components.interactive

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.components.primitives.IenText

@Composable
fun IenCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String? = null,
) {
    Row(
        modifier = modifier
            .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
            .clickable(enabled = enabled, role = Role.Checkbox) { onCheckedChange(!checked) },
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null,
            enabled = enabled,
            colors = CheckboxDefaults.colors(
                checkedColor = IenTheme.colors.brand,
                uncheckedColor = IenTheme.colors.borderStrong,
                checkmarkColor = IenTheme.colors.surface,
            ),
        )
        if (label != null) {
            IenText(
                text = label,
                style = IenTheme.typography.body2,
                color = if (enabled) IenTheme.colors.textPrimary else IenTheme.colors.textDisabled,
            )
        }
    }
}

@Composable
fun IenSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = IenTheme.colors.surface,
            checkedTrackColor = IenTheme.colors.brand,
            uncheckedThumbColor = IenTheme.colors.surface,
            uncheckedTrackColor = IenTheme.colors.borderStrong,
        ),
    )
}

@Composable
fun IenSegmentedControl(
    items: List<String>,
    selectedIndex: Int,
    onSelectedIndexChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    IenSurface(
        modifier = modifier,
        color = IenTheme.colors.surfaceWeak,
        shape = RoundedCornerShape(IenTheme.radius.default),
        border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border),
    ) {
        Row(
            modifier = Modifier.padding(IenTheme.spacing.xxs),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxs),
        ) {
            items.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                IenSurface(
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = IenTheme.state.minimumTouchTarget)
                        .clickable(enabled = enabled) { onSelectedIndexChange(index) },
                    color = if (selected) IenTheme.colors.surface else IenTheme.colors.surfaceWeak,
                    contentColor = if (selected) IenTheme.colors.textPrimary else IenTheme.colors.textSecondary,
                    shape = RoundedCornerShape(IenTheme.radius.sm),
                ) {
                    IenText(
                        text = label,
                        modifier = Modifier.padding(horizontal = IenTheme.spacing.sm, vertical = IenTheme.spacing.xs),
                        style = IenTheme.typography.label2,
                        color = if (selected) IenTheme.colors.textPrimary else IenTheme.colors.textSecondary,
                    )
                }
            }
        }
    }
}
