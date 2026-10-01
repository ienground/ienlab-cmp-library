package zone.ien.utils.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.ui.foundation.IenColorScheme
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenSurface
import zone.ien.utils.ui.primitives.IenText

internal data class IenTooltipColors(
    val container: Color,
    val content: Color,
)

internal fun resolveIenTooltipColors(
    tone: IenSemanticTone,
    colors: IenColorScheme,
): IenTooltipColors = when (tone) {
    IenSemanticTone.Neutral -> IenTooltipColors(
        container = colors.surfaceRaised,
        content = colors.textPrimary,
    )
    IenSemanticTone.Brand -> IenTooltipColors(
        container = colors.brand,
        content = colors.onBrand,
    )
    IenSemanticTone.Success -> IenTooltipColors(
        container = colors.success,
        content = colors.onSuccess,
    )
    IenSemanticTone.Warning -> IenTooltipColors(
        container = colors.warning,
        content = colors.onWarning,
    )
    IenSemanticTone.Danger -> IenTooltipColors(
        container = colors.danger,
        content = colors.onDanger,
    )
    IenSemanticTone.Info -> IenTooltipColors(
        container = colors.info,
        content = colors.onInfo,
    )
}

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
                positioning = positioning,
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
    positioning: TooltipAnchorPosition = TooltipAnchorPosition.Below,
    label: String
) {
    val colors = resolveIenTooltipColors(IenSemanticTone.Neutral, IenTheme.colors)
    val shape = ContinuousRoundedRectangle(12.dp)
    val outerPadding = when (positioning) {
        TooltipAnchorPosition.Above -> PaddingValues(start = 16.dp, end = 16.dp, top = 32.dp)
        TooltipAnchorPosition.Below -> PaddingValues(start = 16.dp, end = 16.dp, bottom = 32.dp)
        else -> PaddingValues(16.dp)
    }
    val contentAlignment = when (positioning) {
        TooltipAnchorPosition.Above -> Alignment.BottomCenter
        TooltipAnchorPosition.Below -> Alignment.TopCenter
        else -> Alignment.Center
    }

    Box(
        modifier = modifier.padding(outerPadding),
        contentAlignment = contentAlignment,
    ) {
        IenSurface(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = shape,
                    clip = false,
                    ambientColor = Color(0x80001D3A),
                    spotColor = Color(0x80001D3A)
                ),
            color = colors.container,
            contentColor = colors.content,
            shape = shape,
        ) {
            IenText(
                text = label,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                style = IenTheme.typography.label2.copy(fontWeight = FontWeight.Bold),
                color = colors.content,
                textAlign = TextAlign.Center,
            )
        }
    }
}
