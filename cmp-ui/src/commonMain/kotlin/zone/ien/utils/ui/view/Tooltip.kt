package zone.ien.utils.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenTheme

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

/**
 * IenTooltipBox는 Ien 디자인 시스템 스타일의 툴팁을 요소를 길게 누르거나 올렸을 때 표시하는 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param positioning 툴팁이 표시될 위치 기준
 * @param isPersistent 툴팁의 지속 여부 (true인 경우 탭 외부를 누를 때까지 툴팁이 유지됨)
 * @param label 툴팁에 표시할 텍스트 내용
 * @param content 툴팁을 표시할 대상 컴포저블
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IenTooltipBox(
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
            IenTooltipText(
                modifier = modifier,
                label = label
            )
        },
        content = content
    )
}

/**
 * IenTooltipText는 툴팁 내부에 텍스트와 배경 스타일을 적용하여 렌더링하는 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param label 표시할 텍스트
 */
@Composable
fun IenTooltipText(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = IenTheme.typography.caption,
        color = IenTheme.colors.textPrimary,
        modifier = modifier
            .background(IenTheme.colors.surfaceRaised, ContinuousRoundedRectangle(IenTheme.radius.xs))
            .padding(horizontal = IenTheme.spacing.xs, vertical = IenTheme.spacing.xxs)
    )
}
