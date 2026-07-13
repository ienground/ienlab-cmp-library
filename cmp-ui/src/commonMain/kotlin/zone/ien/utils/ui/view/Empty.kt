package zone.ien.utils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.components.foundation.IenTheme

/**
 * Empty는 비어 있는 상태를 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 아이콘
 * @param title 제목
 * @param content 내용
 * @param buttons 버튼들
 */
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    icon: (@Composable (Modifier) -> Unit)?,
    title: @Composable () -> Unit,
    content: (@Composable () -> Unit)? = null,
    buttons: @Composable (RowScope.() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        CompositionLocalProvider(
            LocalContentColor provides IenTheme.colors.textPrimary,
        ) {
            icon?.let {
                it(
                    Modifier
                        .background(IenTheme.colors.surfaceVariant, RoundedCornerShape(IenTheme.radius.sm))
                        .padding(8.dp)
                        .size(36.dp)
                )
            }
        }
        ProvideTextStyle(
            value = IenTheme.typography.title2.copy(
                color = IenTheme.colors.textPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        ) {
            Box(
                modifier = Modifier.padding(top = 8.dp)
            ) {
                title()
            }
        }
        ProvideTextStyle(
            value = IenTheme.typography.body2.copy(
                color = IenTheme.colors.textSecondary,
                textAlign = TextAlign.Center
            )
        ) {
            content?.invoke()
        }
        buttons?.let {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                content = it,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyPreview() {
    IenTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Empty(
                icon = {
                    Icon(
                        imageVector = M3SystemIcons.Edit,
                        contentDescription = null,
                        modifier = it
                    )
                },
                title = { Text(text = "Title is Here") },
                content = {
                    Text(text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolo")
                },
                buttons = {
                    Button(
                        onClick = {}
                    ) { Text(text = "hi") }
                    Button(
                        onClick = {}
                    ) { Text(text = "hi") }
                },
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }
    }
}
