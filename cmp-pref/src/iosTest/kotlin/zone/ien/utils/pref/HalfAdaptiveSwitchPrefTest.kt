package zone.ien.utils.pref

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.isToggleable
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.uikit.LocalUIViewController
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import platform.UIKit.UIViewController
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.pref.item.SwitchPref
import zone.ien.utils.pref.item.TextPref
import zone.ien.utils.ui.section.IenSectionLinkDefault

class HalfAdaptiveSwitchPrefTest {
    @OptIn(ExperimentalTestApi::class, ExperimentalAdaptiveApi::class)
    @Test
    fun halfAdaptiveKeepsIenRowStyleAndTogglesFromTitle() = runComposeUiTest {
        val key = booleanPreferencesKey("enabled")
        val dataStore = object : DataStore<Preferences> {
            override val data = MutableStateFlow(emptyPreferences())

            override suspend fun updateData(transform: suspend (Preferences) -> Preferences): Preferences {
                return transform(data.value).also { data.value = it }
            }
        }
        var materialThumbShown = false

        setContent {
            CompositionLocalProvider(LocalUIViewController provides UIViewController()) {
                IenAdaptiveTheme(target = Theme.Cupertino) {
                    PrefsScreen(dataStore = dataStore, type = PrefsScreenType.HalfAdaptive) {
                        PrefsGroup {
                            TextPref(title = "일반 항목")
                            SwitchPref(
                                title = "스위치 항목",
                                key = key,
                                defaultValue = false,
                                colors = IenSectionLinkDefault.colors(),
                                thumbContent = {
                                    materialThumbShown = true
                                    Text("손잡이")
                                },
                            )
                        }
                    }
                }
            }
        }

        val regularLayouts = mutableListOf<TextLayoutResult>()
        val switchLayouts = mutableListOf<TextLayoutResult>()
        onNodeWithText("일반 항목").performSemanticsAction(SemanticsActions.GetTextLayoutResult) {
            it(regularLayouts)
        }
        onNodeWithText("스위치 항목").performSemanticsAction(SemanticsActions.GetTextLayoutResult) {
            it(switchLayouts)
        }
        assertTrue(regularLayouts.isNotEmpty())
        assertTrue(switchLayouts.isNotEmpty())
        assertEquals(regularLayouts.single().layoutInput.style, switchLayouts.single().layoutInput.style)
        assertEquals(false, materialThumbShown)

        onNodeWithText("스위치 항목").performClick()
        waitUntil { dataStore.data.value[key] == true }
        assertEquals(true, dataStore.data.value[key])

        onNode(isToggleable()).assertIsOn().performClick()
        waitUntil { dataStore.data.value[key] == false }
        assertEquals(false, dataStore.data.value[key])
    }
}
