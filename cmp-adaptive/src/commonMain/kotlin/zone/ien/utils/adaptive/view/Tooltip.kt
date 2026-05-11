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

/**
 * 적응형 툴팁 박스 컴포저블
 * 
 * Material 및 Cupertino 플랫폼에 따라 다르게 동작하는 툴팁 박스를 제공합니다.
 * 
 * @param modifier 툴팁에 적용할 수정자
 * @param positioning 툴팁의 위치 지정 방식
 * @param isPersistent 툴팁이 지속적으로 표시될지 여부
 * @param label 툴팁에 표시할 라벨 텍스트
 * @param content 툴팁이 적용될 콘텐츠 컴포저블
 */
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