package zone.ien.utils.hig.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import zone.ien.hig.theme.CupertinoColors
import zone.ien.hig.theme.systemRed

@Composable
fun HigAsteriskTextWrapper(
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        ProvideTextStyle(
            value = style
        ) {
            content()
            Text(
                text = "*",
                color = CupertinoColors.systemRed,
            )
        }
    }
}