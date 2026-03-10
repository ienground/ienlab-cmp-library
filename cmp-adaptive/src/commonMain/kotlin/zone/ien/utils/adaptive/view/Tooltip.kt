package zone.ien.utils.adaptive.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.hig.view.HigTooltipBox
import zone.ien.utils.ui.view.BaseTooltipBox
import zone.ien.utils.ui.view.M3TooltipBox
import zone.ien.utils.ui.view.M3TooltipText

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveTooltipBox(
    modifier: Modifier = Modifier,
    positioning: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    isPersistent: Boolean = false,
    label: String,
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3TooltipBox(
                modifier = modifier,
                positioning = positioning,
                isPersistent = isPersistent,
                label = label,
                content = content
            )
        },
        cupertino = {
            HigTooltipBox(
                modifier = modifier,
                positioning = positioning,
                isPersistent = isPersistent,
                label = label,
                content = content
            )
        }
    )
}