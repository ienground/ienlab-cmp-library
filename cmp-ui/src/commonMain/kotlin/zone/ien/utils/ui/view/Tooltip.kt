package zone.ien.utils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * BaseTooltipBox는 툴팁 박스를 표시하기 위한 내부 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param positioning 위치 지정
 * @param isPersistent 지속 여부
 * @param label 라벨
 * @param tooltipText 툴팁 텍스트
 * @param content 내용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTooltipBox(
    modifier: Modifier = Modifier,
    positioning: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    isPersistent: Boolean = false,
    label: String,
    tooltipText: @Composable (Modifier, String) -> Unit,
    content: @Composable () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(positioning = positioning),
        state = rememberTooltipState(isPersistent = isPersistent),
        tooltip = {
            tooltipText(Modifier, label)
        },
        content = content,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun M3TooltipBox(
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
            M3TooltipText(
                modifier = modifier,
                label = label
            )
        },
        content = content
    )
}

@Composable
fun M3TooltipText(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceContainerHighest, RoundedCornerShape(4.dp))
            .padding(4.dp)
    )
}