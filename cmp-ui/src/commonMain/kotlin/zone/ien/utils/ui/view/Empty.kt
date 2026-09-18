package zone.ien.utils.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import zone.ien.utils.icon.material.M3SystemIcons
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.primitives.IenAssetFrame
import zone.ien.utils.ui.primitives.IenAssetFrameShape
import zone.ien.utils.ui.primitives.IenAssetFrameSize
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.primitives.IenProvideTextStyle
import zone.ien.utils.ui.primitives.IenText

/**
 * [Empty]의 아이콘과 아이콘을 감싸는 [IenAssetFrame]의 설정을 정의하는 스코프입니다.
 */
class EmptyIconScope internal constructor() {
    /** 아이콘을 감싸는 [IenAssetFrame]에 적용할 Modifier입니다. */
    var modifier: Modifier = Modifier

    /** 아이콘을 감싸는 [IenAssetFrame]의 크기입니다. */
    var size: IenAssetFrameSize = IenAssetFrameSize.Large

    /** 아이콘을 감싸는 [IenAssetFrame]의 시맨틱 톤입니다. */
    var tone: IenSemanticTone = IenSemanticTone.Neutral

    /** 아이콘을 감싸는 [IenAssetFrame]의 형상입니다. */
    var shape: IenAssetFrameShape = IenAssetFrameShape.Rounded

    /** 아이콘을 감싸는 [IenAssetFrame]의 테두리 표시 여부입니다. */
    var bordered: Boolean = false

    /** 아이콘을 감싸는 [IenAssetFrame]의 접근성 설명입니다. */
    var contentDescription: String? = null

    /** 아이콘을 감싸는 [IenAssetFrame] 내부 콘텐츠의 정렬입니다. */
    var contentAlignment: Alignment = Alignment.Center

    /**
     * 설정된 파라미터로 아이콘을 감싸는 [IenAssetFrame]을 렌더링합니다.
     *
     * @param content 프레임 안에 표시할 아이콘 콘텐츠
     */
    @Composable
    fun content(content: @Composable (Modifier) -> Unit) {
        IenAssetFrame(
            modifier = modifier,
            size = size,
            tone = tone,
            shape = shape,
            bordered = bordered,
            contentDescription = contentDescription,
            contentAlignment = contentAlignment,
        ) {
            content(Modifier.size(36.dp))
        }
    }
}

/**
 * Empty는 비어 있는 상태를 표시하기 위한 컴포저블입니다.
 *
 * @param modifier 적용할 Modifier
 * @param icon 아이콘과 아이콘 프레임 설정을 정의하는 [EmptyIconScope] 콘텐츠
 * @param title 제목
 * @param content 내용
 * @param buttons 버튼들
 */
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    icon: (@Composable EmptyIconScope.() -> Unit)?,
    title: @Composable () -> Unit,
    content: (@Composable () -> Unit)? = null,
    buttons: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        icon?.let { iconContent ->
            val iconScope = EmptyIconScope()
            iconContent(iconScope)
        }
        IenProvideTextStyle(
            style = IenTheme.typography.title2.copy(
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
        IenProvideTextStyle(
            style = IenTheme.typography.body2.copy(
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
                    content { modifier ->
                        IenIcon(
                            imageVector = M3SystemIcons.Edit,
                            contentDescription = null,
                            modifier = modifier,
                        )
                    }
                },
                title = { IenText(text = "Title is Here") },
                content = {
                    IenText(text = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolo")
                },
                buttons = {
                    IenButton(onClick = {}) { IenText("hi") }
                    IenButton(onClick = {}) { IenText("hi") }
                },
                modifier = Modifier.fillMaxWidth(0.75f)
            )
        }
    }
}
