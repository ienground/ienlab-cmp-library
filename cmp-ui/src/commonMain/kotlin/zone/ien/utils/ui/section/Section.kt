package zone.ien.utils.ui.section

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastSumBy
import zone.ien.hig.section.SectionScope
import zone.ien.utils.ui.components.foundation.IenTheme
import zone.ien.utils.ui.components.primitives.IenProvideTextStyle
import zone.ien.utils.ui.components.primitives.IenSurface
import zone.ien.utils.ui.utils.conditional

/**
 * 섹션의 컨텍스트 스코프 구현체
 * 
 * 이 객체는 섹션 내부에서 사용되는 컨텍스트를 제공하며, 
 * 섹션 내부 컴포저블들이 공유할 수 있는 정보를 포함합니다.
 */
@Stable
internal object SectionScopeImpl: SectionScope

/**
 * Material3 스타일의 섹션 배경을 적용하는 Modifier
 * 
 * 이 Modifier는 컴포저블 요소에 Material3의 surfaceContainer 색상을 적용하여
 * 섹션의 배경 스타일을 적용합니다.
 * 
 * @return 배경이 적용된 Modifier
 */
@Composable
fun Modifier.m3SectionBackground(): Modifier {
    return this.background(IenTheme.colors.surfaceWeak)
}

/**
 * Material3 스타일의 섹션 스타일을 적용하는 컴포저블
 * 
 * 이 컴포저블은 섹션의 전반적인 스타일을 제공하며, 
 * 스크롤 가능 여부, 모양(shape), 제목, 내용을 정의할 수 있습니다.
 * 
 * @param modifier 섹션에 적용할 Modifier
 * @param fullHeight 섹션의 높이를 화면 전체로 설정할지 여부
 * @param scrollState 섹션 내부의 스크롤 상태를 관리하는 ScrollState
 * @param shape 섹션의 모양을 정의하는 Shape
 * @param title 섹션의 제목을 표시하는 컴포저블
 * @param content 섹션의 내용을 표시하는 컴포저블 블록
 */
@Composable
fun M3ProvideSectionStyle(
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = rememberScrollState(),
    shape: Shape = RectangleShape,
    title: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .conditional(fullHeight) { fillMaxHeight() }
            .conditional(scrollState != null) { scrollState?.let { this.verticalScroll(it) } ?: this }
            .clip(shape)
            .m3SectionBackground()
            .then(modifier)
        ,
        content = {
            if (title != null) {
                title()
            } else {
                Spacer(modifier = Modifier)
            }
            content()
            Spacer(modifier = Modifier)
        }
    )
}

/**
 * Material3 스타일의 섹션 컴포저블
 * 
 * 이 컴포저블은 제목, 내용, 캡션을 포함하는 섹션을 제공합니다.
 * 섹션 내부의 컴포저블들은 정의된 스타일에 따라 표시됩니다.
 * 
 * @param modifier 섹션에 적용할 Modifier
 * @param title 섹션의 제목을 표시하는 컴포저블
 * @param caption 섹션의 캡션을 표시하는 컴포저블
 * @param content 섹션의 내용을 정의하는 SectionScope 블록
 */
@Composable
fun M3Section(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    Column(
        modifier = modifier
    ) {
        IenProvideTextStyle(
            style = IenTheme.typography.label1,
            color = IenTheme.colors.textSecondary,
        ) {
            title?.let {
                Box(
                    modifier = Modifier
                        .padding(bottom = IenTheme.spacing.xs)
                        .padding(horizontal = IenTheme.spacing.xl),
                ) { it() }
            }
        }
        val itemGap = IenTheme.stroke.hairline
        IenSurface(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(IenTheme.radius.lg)),
            color = IenTheme.colors.surfaceRaised,
            shape = RoundedCornerShape(IenTheme.radius.lg),
            border = BorderStroke(IenTheme.stroke.thin, IenTheme.colors.border.copy(alpha = 0.72f)),
        ) {
            SubcomposeLayout { constraints ->
                val gap = itemGap.toPx().toInt()
                val measurables = subcompose(null) { content(SectionScopeImpl) }

                val placeables = measurables.fastMap { it.measure(constraints) }

                layout(
                    width = constraints.maxWidth,
                    height = (placeables.fastSumBy { it.height } + gap * (placeables.size - 1)).coerceAtLeast(0)
                ) {
                    var h = 0
                    placeables.fastForEachIndexed { i, p ->
                        p.place(0, h)
                        h += p.height
                        if (i < placeables.lastIndex) {
                            h += gap
                        }
                    }
                }
            }
        }
        IenProvideTextStyle(
            style = IenTheme.typography.caption,
            color = IenTheme.colors.textTertiary,
        ) {
            caption?.let {
                Box(
                    modifier = Modifier
                        .padding(top = IenTheme.spacing.xs)
                        .padding(horizontal = IenTheme.spacing.xl),
                ) { it() }
            }
        }
    }
}
