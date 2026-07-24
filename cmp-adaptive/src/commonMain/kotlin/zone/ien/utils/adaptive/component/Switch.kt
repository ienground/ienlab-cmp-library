package zone.ien.utils.adaptive.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import zone.ien.hig.CupertinoSwitch
import zone.ien.hig.ExperimentalCupertinoApi
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.ui.interactive.IenSwitch

@OptIn(ExperimentalAdaptiveApi::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
) {
    AdaptiveWidget(
        material = {
            IenSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                thumbContent = thumbContent,
                interactionSource = interactionSource,
            )
        },
        cupertino = {
            CupertinoSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = modifier,
                enabled = enabled,
                interactionSource = interactionSource ?: remember { MutableInteractionSource() },
                backdrop = rememberDefaultBackdrop(),
            )
        },
    )
}
