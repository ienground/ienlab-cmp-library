package zone.ien.utils.ui.view

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
import zone.ien.utils.ui.foundation.IenTheme

/**
 * IenAsteriskTextWrapper는 별표(*)를 표시하는 텍스트 래퍼 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param style 텍스트 스타일
 * @param content 내용
 */
@Composable
fun IenAsteriskTextWrapper(
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
                color = IenTheme.colors.danger
            )
        }
    }
}
