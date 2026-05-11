package zone.ien.utils.pref

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.kyant.backdrop.backdrops.LayerBackdrop
import zone.ien.hig.section.SectionStyle
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.section.AdaptiveProvideSectionStyle

/**
 * A Composable function that creates a complete preferences screen with data store support.
 * 
 * This function sets up the preferences screen with a proper data store context, 
 * scroll state, and styling. It provides a foundation for building preference screens
 * that persist user settings using DataStore.
 * 
 * @param dataStore The DataStore instance used for storing preferences
 * @param title Composable that displays the screen title
 * @param modifier Modifier to be applied to the layout
 * @param fullHeight Whether the screen should take full height
 * @param scrollState The scroll state to be used for scrolling
 * @param shape The shape of the screen's container
 * @param backdrop The backdrop configuration for the screen
 * @param content Composable content block that contains the preference items
 */
@Composable
fun PrefsScreen(
    dataStore: DataStore<Preferences>,
    title: @Composable () -> Unit = {},
    modifier: Modifier = Modifier,
    fullHeight: Boolean = true,
    scrollState: ScrollState? = rememberScrollState(),
    shape: Shape = RectangleShape,
    backdrop: LayerBackdrop = rememberDefaultBackdrop(),
    content: @Composable (ColumnScope.() -> Unit)
) {
    LocalPrefsDataStore = staticCompositionLocalOf { dataStore }

    CompositionLocalProvider(LocalPrefsDataStore provides dataStore) {
        AdaptiveProvideSectionStyle(
            style = SectionStyle.InsetGrouped,
            modifier = modifier,
            fullHeight = fullHeight,
            scrollState = scrollState,
            shape = shape,
            backdrop = backdrop,
            title = title,
            content = {
                content()
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
    }
}

/**
 * 설정 컴포넌트에 DataStore를 제공하기 위한 CompositionLocal입니다.
 * 이를 통해 DataStore 인스턴스를 직접 전달하지 않고도 접근할 수 있습니다.
 */
val LocalPrefsDataStore = staticCompositionLocalOf<DataStore<Preferences>> {
    error("No DataStore provided. Ensure content is wrapped in PrefsScreen.")
}