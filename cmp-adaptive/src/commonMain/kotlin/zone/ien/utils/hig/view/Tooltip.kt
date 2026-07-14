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
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.hig.theme.CupertinoTheme
import zone.ien.utils.ui.view.BaseTooltipBox


/**
 * HIG 툴팁 박스 컴포저블
 * 
 * 사용자 정의 토스트 박스 레이아웃을 제공합니다.
 * 
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param positioning 토스트 박스의 위치 지정 (위, 아래 등)
 * @param isPersistent 토스트 박스가 항상 표시되는지 여부
 * @param label 토스트 박스에 표시되는 텍스트
 * @param content 토스트 박스가 적용될 내용
 * @return 사용자 정의 토스트 박스 컴포저블
 */
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

/**
 * HIG 툴팁 텍스트 컴포저블
 * 
 * 토스트 박스에 표시되는 텍스트를 렌더링합니다.
 * 
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param label 토스트 박스에 표시되는 텍스트
 * @return 토스트 박스 텍스트 컴포저블
 */
@Composable
fun HigTooltipText(
    modifier: Modifier = Modifier,
    label: String
) {
    Text(
        text = label,
        style = CupertinoTheme.typography.caption1,
        modifier = modifier
            .background(CupertinoTheme.colorScheme.systemFill, ContinuousRoundedRectangle(4.dp))
            .padding(4.dp)
    )
}