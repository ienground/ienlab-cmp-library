package zone.ien.utils.pref

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSection

@Composable
fun ColumnScope.PrefsGroup(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    caption: (@Composable () -> Unit)? = null,
    content: @Composable SectionScope.() -> Unit
) {
    AdaptiveSection(
        modifier = modifier,
        title = title,
        caption = caption,
        content = content
    )
}