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

lateinit var LocalPrefsDataStore: ProvidableCompositionLocal<DataStore<Preferences>>

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
            content = {
                title()
                content()
                Spacer(modifier = Modifier.height(16.dp))
            }
        )
    }
}