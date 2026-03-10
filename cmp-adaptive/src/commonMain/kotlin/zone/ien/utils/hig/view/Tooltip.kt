package zone.ien.utils.hig.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.ui.view.BaseTooltipBox


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HigTooltipBox(
    modifier: Modifier = Modifier,
    positioning: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    isPersistent: Boolean = false,
    label: String,
    content: @Composable () -> Unit
) {
    BaseTooltipBox(
        modifier = modifier,
        positioning = positioning,
        isPersistent = isPersistent,
        label = label,
        tooltipText = { modifier, label ->
            HigTooltipText(
                modifier = modifier,
                label = label
            )
        },
        content = content
    )
}

@Composable
fun HigTooltipText(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = CupertinoTheme.typography.caption1,
        modifier = modifier
            .background(CupertinoTheme.colorScheme.systemFill, RoundedCornerShape(4.dp))
            .padding(4.dp)
    )
}