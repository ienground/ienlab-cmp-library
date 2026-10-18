package zone.ien.utils.ui.interactive

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.FloatingActionButtonMenuScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.toggleableState
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenProvideTextStyle

/** [IenFabMenu] 안에서 메뉴 항목을 배치하는 범위입니다. */
@Stable
class IenFabMenuScope internal constructor(
    internal val materialScope: FloatingActionButtonMenuScope,
)

/**
 * FAB 위로 관련 동작을 순차적으로 펼치는 메뉴입니다.
 *
 * [expanded]는 호출부에서 관리하며, 항목 선택 후 닫기와 화면 뒤로 가기 처리도 호출부에서 결정합니다.
 * 항목은 [IenFabMenuItem]으로 구성하고, [button]에는 [IenFabMenuButton]이나 기존 [IenFab]을 배치합니다.
 * 접힌 상태에서는 [button]과 같은 크기를 차지하며, 메뉴의 내부 여백은 FAB의 바깥 여백에 추가하지 않습니다.
 * 화면 전체를 덮지 않으므로 Scaffold의 플로팅 버튼 영역 등에 배치할 수 있습니다.
 *
 * @param expanded 메뉴를 펼칠지 여부입니다.
 * @param button 메뉴를 열고 닫는 버튼입니다.
 * @param modifier 메뉴 전체에 적용할 Modifier입니다.
 * @param horizontalAlignment 버튼과 메뉴 항목의 가로 정렬입니다. RTL 배치를 지원합니다.
 * @param content 위에서 아래 순서로 표시할 메뉴 항목입니다.
 */
@Composable
fun IenFabMenu(
    expanded: Boolean,
    button: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.End,
    content: @Composable IenFabMenuScope.() -> Unit,
) {
    val buttonMeasurement = remember { FabMenuButtonMeasurement() }
    Layout(
        modifier = modifier,
        content = {
            FloatingActionButtonMenu(
                expanded = expanded,
                button = {
                    Box(
                        modifier = Modifier.layout { measurable, constraints ->
                            val placeable = measurable.measure(constraints)
                            buttonMeasurement.size = IntSize(placeable.width, placeable.height)
                            layout(placeable.width, placeable.height) { placeable.place(0, 0) }
                        },
                    ) {
                        button()
                    }
                },
                horizontalAlignment = horizontalAlignment,
            ) {
                val scope = remember(this) { IenFabMenuScope(this) }
                scope.content()
            }
        },
    ) { measurables, constraints ->
        val horizontalPadding = MaterialFabMenuHorizontalPadding.roundToPx()
        val bottomPadding = MaterialFabMenuBottomPadding.roundToPx()
        val menu = measurables.single().measure(
            constraints
                .offset(horizontal = horizontalPadding * 2, vertical = bottomPadding)
                .copy(minWidth = 0, minHeight = 0),
        )
        val buttonSize = buttonMeasurement.size
        val menuContentWidth = (menu.width - horizontalPadding * 2).coerceAtLeast(0)
        val contentWidth = if (expanded) menuContentWidth else buttonSize.width
        val contentHeight = if (expanded) menu.height - bottomPadding else buttonSize.height
        val width = contentWidth.coerceIn(constraints.minWidth, constraints.maxWidth)
        val height = contentHeight.coerceAtLeast(0).coerceIn(constraints.minHeight, constraints.maxHeight)
        val nativeButtonX = horizontalPadding +
            horizontalAlignment.align(buttonSize.width, menuContentWidth, layoutDirection)
        val nativeButtonY = menu.height - buttonSize.height - bottomPadding
        val buttonX = horizontalAlignment.align(buttonSize.width, width, layoutDirection)
        val buttonY = height - buttonSize.height
        layout(width, height) {
            menu.place(buttonX - nativeButtonX, buttonY - nativeButtonY)
        }
    }
}

private class FabMenuButtonMeasurement {
    var size: IntSize = IntSize.Zero
}

private val MaterialFabMenuHorizontalPadding = 16.dp
private val MaterialFabMenuBottomPadding = 16.dp

/**
 * 펼침 상태에 따라 열기 아이콘과 닫기 아이콘을 전환하는 Ien FAB입니다.
 *
 * @param expanded 연결된 메뉴의 펼침 상태입니다.
 * @param onExpandedChange 버튼을 누를 때 요청할 다음 펼침 상태입니다.
 * @param contentDescription 현재 동작을 설명할 접근성 라벨입니다.
 * @param modifier 버튼에 적용할 Modifier입니다.
 * @param tone 버튼의 의미적 강조 색상입니다.
 * @param state 버튼의 활성화 및 로딩 상태입니다.
 * @param colors 기존 Ien 버튼과 동일한 색상 및 배경 브러시입니다.
 * @param icon 메뉴가 닫혀 있을 때 표시할 아이콘입니다.
 * @param closeIcon 메뉴가 열려 있을 때 표시할 아이콘입니다.
 */
@Composable
fun IenFabMenuButton(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    contentDescription: String,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    state: IenButtonState = IenButtonState(),
    colors: IenButtonColors = IenButtonDefault.colors(tone = tone),
    icon: @Composable () -> Unit = {
        IenIcon(IconData.Vector(SystemIcons.Add), contentDescription = null)
    },
    closeIcon: @Composable () -> Unit = {
        IenIcon(IconData.Vector(SystemIcons.Close), contentDescription = null)
    },
) {
    IenFab(
        onClick = { onExpandedChange(!expanded) },
        modifier = modifier.semantics {
            this.contentDescription = contentDescription
            toggleableState = ToggleableState(expanded)
        },
        tone = tone,
        state = state,
        colors = colors,
    ) {
        Crossfade(
            targetState = expanded,
            animationSpec = tween(IenTheme.motion.fastMillis),
            label = "IenFabMenuButton",
        ) { isExpanded ->
            if (isExpanded) closeIcon() else icon()
        }
    }
}

/**
 * FAB 메뉴의 아이콘과 라벨이 있는 캡슐 형태 동작입니다.
 * 순차 등장과 사라짐, 좁은 높이에서의 스크롤 및 키보드 탐색은 메뉴에서 처리합니다.
 *
 * @param onClick 항목 선택 시 실행할 동작입니다. 메뉴를 닫으려면 호출부의 펼침 상태를 변경합니다.
 * @param text 항목의 라벨입니다.
 * @param icon 항목의 아이콘입니다.
 * @param modifier 항목에 적용할 Modifier입니다.
 * @param tone 항목의 의미적 강조 색상입니다.
 * @param containerColor 항목의 배경색입니다.
 * @param contentColor 라벨과 아이콘의 색상입니다.
 */
@Composable
fun IenFabMenuScope.IenFabMenuItem(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    tone: IenSemanticTone = IenSemanticTone.Brand,
    containerColor: Color = IenButtonDefault.colors(IenButtonVariant.Weak, tone).container,
    contentColor: Color = IenButtonDefault.colors(IenButtonVariant.Weak, tone).content,
) {
    with(materialScope) {
        FloatingActionButtonMenuItem(
            onClick = onClick,
            text = {
                IenProvideTextStyle(IenTheme.typography.label1, contentColor, text)
            },
            icon = icon,
            modifier = modifier,
            containerColor = containerColor,
            contentColor = contentColor,
        )
    }
}
