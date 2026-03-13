package zone.ien.utils.pref.item

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.AdaptiveSectionSwitchItem
import zone.ien.utils.pref.LocalPrefsDataStore

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.SwitchPref(
    modifier: Modifier = Modifier,
    title: String,
    summaryOn: String? = null,
    summaryOff: String? = null,
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean,
    onCheckedChange: ((Boolean) -> Unit)? = null,
    enabled: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    val coroutineScope = rememberCoroutineScope()
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var checked = defaultValue
    prefs?.get(key)?.also { checked = it }

    fun edit(newValue: Boolean) = run {
        coroutineScope.launch {
            try {
                dataStore.edit { it[key] = newValue }
                checked = newValue
                onCheckedChange?.invoke(newValue)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    AdaptiveSectionSwitchItem(
        modifier = modifier,
        leadingContent = leadingIcon,
        checked = checked,
        onCheckedChange = { edit(it) },
        enabled = enabled,
        supportingContent =
            if (checked) summaryOn?.let { { Text(text = it) } }
            else summaryOff?.let { { Text(text = it) } },
        title = { Text(text = title) }
    )
}