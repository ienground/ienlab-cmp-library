package zone.ien.utils.ui.interactive

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import com.kyant.capsule.ContinuousRoundedRectangle
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon

/**
 * 주 동작과 관련된 보조 동작을 각각 실행하는 분할 버튼입니다.
 *
 * [expanded]는 호출자가 관리하며, 메뉴는 [onTrailingClick]과 기존 메뉴 컴포넌트를 연결합니다.
 * 로딩 중에는 두 동작을 모두 차단하고 주 버튼에만 로더를 표시합니다.
 *
 * @param onClick 주 동작을 실행하는 콜백입니다.
 * @param onTrailingClick 보조 동작을 실행하는 콜백입니다.
 * @param trailingContentDescription 보조 버튼의 접근성 설명입니다.
 * @param modifier 전체 버튼의 배치에 적용합니다.
 * @param expanded 보조 동작의 펼침 상태입니다. 기본 화살표의 회전과 모서리에 반영합니다.
 * @param size 두 버튼의 높이와 주 버튼의 내부 여백을 결정합니다.
 * @param variant 두 버튼에 공통으로 적용할 표시 방식입니다.
 * @param tone 두 버튼에 공통으로 적용할 의미적 색상입니다.
 * @param state 전체 버튼의 활성화 및 로딩 상태입니다.
 * @param colors 두 버튼에 공통으로 적용할 색상과 브러시입니다.
 * @param display 전체 버튼의 가로 배치 방식입니다.
 * @param trailingContent 보조 버튼의 콘텐츠입니다. 기본값은 펼침 상태에 따라 회전하는 화살표입니다.
 * @param content 주 버튼의 콘텐츠입니다.
 */
@Composable
fun IenSplitButton(
    onClick: () -> Unit,
    onTrailingClick: () -> Unit,
    trailingContentDescription: String,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    size: IenButtonSize = IenButtonSize.Large,
    variant: IenButtonVariant = IenButtonVariant.Fill,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    colors: IenButtonColors = IenButtonDefault.colors(variant = variant, tone = tone),
    display: IenButtonDisplay = IenButtonDisplay.Inline,
    trailingContent: @Composable () -> Unit = {
        val rotation by animateFloatAsState(if (expanded) 180f else 0f)
        IenIcon(
            icon = IconData.Vector(SystemIcons.ChevronDown),
            modifier = Modifier.graphicsLayer { rotationZ = rotation },
        )
    },
    content: @Composable () -> Unit,
) {
    val fillWidth = display != IenButtonDisplay.Inline
    val outerRadius = if (display == IenButtonDisplay.Full) IenTheme.radius.none else size.buttonHeight() / 2
    val innerRadius = if (size == IenButtonSize.Large) IenTheme.radius.sm else IenTheme.radius.xs
    val trailingInnerRadius by animateDpAsState(
        targetValue = if (expanded) outerRadius else innerRadius,
        label = "splitButtonTrailingRadius",
    )

    Row(
        modifier = modifier
            .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xxxs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IenButton(
            onClick = onClick,
            modifier = Modifier.weight(1f, fill = fillWidth).fillMaxHeight(),
            size = size,
            variant = variant,
            tone = tone,
            state = state,
            shape = ContinuousRoundedRectangle(
                topStart = outerRadius,
                bottomStart = outerRadius,
                topEnd = innerRadius,
                bottomEnd = innerRadius,
            ),
            colors = colors,
            content = content,
        )
        IenButton(
            onClick = onTrailingClick,
            modifier = Modifier
                .width(size.buttonHeight())
                .fillMaxHeight()
                .semantics {
                    contentDescription = trailingContentDescription
                    toggleableState = ToggleableState(expanded)
                },
            size = size,
            variant = variant,
            tone = tone,
            state = IenButtonState(enabled = state.enabled && !state.loading),
            shape = ContinuousRoundedRectangle(
                topStart = trailingInnerRadius,
                bottomStart = trailingInnerRadius,
                topEnd = outerRadius,
                bottomEnd = outerRadius,
            ),
            contentPadding = PaddingValues(IenTheme.spacing.none),
            colors = colors,
            content = trailingContent,
        )
    }
}
