package zone.ien.utils.ui.section

import androidx.compose.foundation.ScrollState
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
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
import zone.ien.utils.ui.utils.conditional

@Stable
internal object SectionScopeImpl: SectionScope

@Composable
fun Modifier.m3SectionBackground(): Modifier {
    return this.background(MaterialTheme.colorScheme.surfaceContainer)
}

@Composable
fun M3ProvideSectionStyle(
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = null,
    shape: Shape = RectangleShape,
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
            Spacer(modifier = Modifier.height(4.dp))
            content()
        }
    )
}

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
        ProvideTextStyle(
            value = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
        ) {
            title?.let {
                Box(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .padding(horizontal = 22.dp),
                ) { it() }
            }
        }
        SubcomposeLayout(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
        ) { constraints ->
            val gap = 2.dp.toPx().toInt()
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
        ProvideTextStyle(
            value = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.primary)
        ) {
            caption?.let {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(horizontal = 22.dp),
                ) { it() }
            }
        }
    }
}