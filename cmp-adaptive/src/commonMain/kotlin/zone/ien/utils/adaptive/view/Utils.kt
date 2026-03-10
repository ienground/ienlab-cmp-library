package zone.ien.utils.adaptive.view

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.utils.hig.view.HigAsteriskTextWrapper
import zone.ien.utils.ui.view.M3AsteriskTextWrapper

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AsteriskTextWrapper(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    content: @Composable () -> Unit
) {
    AdaptiveWidget(
        material = {
            M3AsteriskTextWrapper(
                modifier = modifier,
                style = style,
                content = content
            )
        },
        cupertino = {
            HigAsteriskTextWrapper(
                modifier = modifier,
                style = style,
                content = content
            )
        }
    )
}