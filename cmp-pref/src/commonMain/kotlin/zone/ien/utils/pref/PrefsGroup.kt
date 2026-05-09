package zone.ien.utils.pref

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSection

/**
 * A Composable function that creates a group of preferences with a title and caption.
 * 
 * This function creates a section in the preferences screen that can contain
 * multiple preference items. It's designed to organize related preferences
 * together for better user experience.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param title Optional composable that displays a title for the preference group
 * @param caption Optional composable that displays a caption for the preference group
 * @param content Composable content block that contains the actual preference items
 */
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