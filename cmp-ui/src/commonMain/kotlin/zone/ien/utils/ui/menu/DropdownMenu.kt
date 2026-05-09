package zone.ien.utils.ui.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.MenuItemColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.ui.utils.crop

internal val DefaultMenuProperties = PopupProperties(focusable = true)

/**
 * M3DropdownMenu는 드롭다운 메뉴를 표시하기 위한 컴포저블입니다.
 *
 * @param expanded 드롭다운이 열려 있는지 여부
 * @param onDismissRequest 드롭다운을 닫기 위한 콜백 함수
 * @param modifier 적용할 Modifier
 * @param offset 오프셋
 * @param scrollState 스크롤 상태
 * @param properties 다이얼로그 속성
 * @param shape 모양
 * @param containerColor 컨테이너 색상
 * @param tonalElevation 톤탈 침강
 * @param shadowElevation 그림자 침강
 * @param border 테두리
 * @param innerPadding 내부 패딩
 * @param content 메뉴 내용
 */
@Composable
fun M3DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    scrollState: ScrollState = rememberScrollState(),
    properties: PopupProperties = DefaultMenuProperties,
    shape: Shape = RoundedCornerShape(16.dp),
    containerColor: Color = MenuDefaults.containerColor,
    tonalElevation: Dp = MenuDefaults.TonalElevation,
    shadowElevation: Dp = MenuDefaults.ShadowElevation,
    border: BorderStroke? = null,
    innerPadding: PaddingValues = PaddingValues(4.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = modifier.crop(vertical = 8.dp),
        offset = offset,
        scrollState = scrollState,
        properties = properties,
        shape = shape,
        containerColor = containerColor,
        tonalElevation = tonalElevation,
        shadowElevation = shadowElevation,
        border = border,
        content = {
            Column(
                modifier = Modifier.padding(innerPadding),
                content = content
            )
        }
    )
}

/**
 * M3DropdownMenuItem은 드롭다운 메뉴 항목을 표시하기 위한 컴포저블입니다.
 *
 * @param text 항목 텍스트
 * @param onClick 항목 클릭 시 호출되는 콜백 함수
 * @param modifier 적용할 Modifier
 * @param leadingIcon leading 아이콘
 * @param trailingIcon trailing 아이콘
 * @param enabled 활성화 여부
 * @param colors 항목 색상
 * @param contentPadding 내용 패딩
 * @param shape 모양
 * @param interactionSource 상호작용 소스
 */
@Composable
fun M3DropdownMenuItem(
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    colors: MenuItemColors = MenuDefaults.itemColors(),
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
    shape: Shape? = RoundedCornerShape(12.dp),
    interactionSource: MutableInteractionSource? = null,
) {
    DropdownMenuItem(
        text = text,
        onClick = onClick,
        modifier = modifier.conditional(shape != null) { shape?.let { clip(it) } ?: this },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    )
}