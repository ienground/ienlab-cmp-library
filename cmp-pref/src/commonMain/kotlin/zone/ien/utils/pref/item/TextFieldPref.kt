package zone.ien.utils.pref.item

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.dialog.AlertDialog
import zone.ien.utils.adaptive.dialog.TextFieldDialog
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.ui.utils.TextFieldDialogData
import zone.ien.utils.utils.checkDecimal

/**
 * A Composable function that creates a text field preference item with a dialog.
 * 
 * This preference item displays a text value that, when clicked, opens a dialog
 * where users can enter or modify text. It handles both string and integer values.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param title The title text for the text preference
 * @param summary Composable function that displays the current value as a summary
 * @param key The Preferences.Key used to identify this preference in the DataStore
 * @param defaultValue The default value for the text field
 * @param enabled Whether the preference is enabled or disabled
 * @param leadingIcon Optional icon to display before the title
 * @param showIcon Whether to show the leading icon
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextFieldPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((String) -> String) = { it },
    key: Preferences.Key<String>,
    defaultValue: String,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var value by remember { mutableStateOf(defaultValue) }
    var textValue by remember(showDialog) { mutableStateOf(value) }

    LaunchedEffect(Unit) {
        prefs?.get(key)?.also { value = it }
    }

    LaunchedEffect(dataStore.data) {
        dataStore.data.collectLatest { it[key]?.also { value = it } }
    }


    fun edit() = run {
        coroutineScope.launch {
            try {
                dataStore.edit { it[key] = textValue }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    TextPref(
        onClick = { if (enabled) showDialog = !showDialog },
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = summary(value),
        leadingIcon = if (showIcon) leadingIcon else null,
        chevron = {}
    )

    TextFieldDialog(
        visible = showDialog,
        icon = leadingIcon,
        title = title,
        textFields = mapOf(
            "value" to TextFieldDialogData()
        ),
        onDismiss = { showDialog = false },
        onConfirm = {
            textValue = it["value"].orEmpty()
            edit()
            showDialog = false
        }
    )
}

/**
 * A Composable function that creates a text field preference item with a conditional enable.
 * 
 * This variant provides the ability to enable/disable the preference based on the
 * value of another boolean preference.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param title The title text for the text preference
 * @param summary Composable function that displays the current value as a summary
 * @param key The Preferences.Key used to identify this preference in the DataStore
 * @param defaultValue The default value for the text field
 * @param enabled Pair of Preferences.Key and default boolean value to enable/disable this preference
 * @param leadingIcon Optional icon to display before the title
 * @param showIcon Whether to show the leading icon
 */
@Composable
fun SectionScope.TextFieldPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((String) -> String) = { it },
    key: Preferences.Key<String>,
    defaultValue: String,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var checked = enabled.second
    prefs?.get(enabled.first)?.also { checked = it }

    TextFieldPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = checked,
        leadingIcon = leadingIcon,
        showIcon = showIcon,
    )
}

/**
 * A Composable function that creates an integer text field preference item.
 * 
 * This preference item displays an integer value and allows editing it in a dialog.
 * It supports validation to ensure only decimal numbers can be entered.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param title The title text for the integer preference
 * @param summary Composable function that displays the current integer value as a summary
 * @param key The Preferences.Key used to identify this preference in the DataStore
 * @param defaultValue The default integer value
 * @param onlyDecimal Whether to only allow decimal inputs
 * @param enabled Whether the preference is enabled or disabled
 * @param leadingIcon Optional icon to display before the title
 * @param showIcon Whether to show the leading icon
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.TextFieldPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Int) -> String) = { it.toString() },
    key: Preferences.Key<Int>,
    defaultValue: Int,
    onlyDecimal: Boolean = true,
    enabled: Boolean = true,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var value by remember { mutableStateOf(defaultValue) }
    var textValue: String by remember(showDialog) { mutableStateOf(value.toString()) }

    LaunchedEffect(Unit) {
        prefs?.get(key)?.also { value = it }
    }

    LaunchedEffect(dataStore.data) {
        dataStore.data.collectLatest { it[key]?.also { value = it } }
    }


    fun edit() = run {
        coroutineScope.launch {
            try {
                dataStore.edit { it[key] = textValue.toInt() }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    TextPref(
        onClick = { if (enabled) showDialog = !showDialog },
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = summary(value),
        leadingIcon = if (showIcon) leadingIcon else null,
        chevron = {},
        adaptation = {
            cupertino {
                isCaption = true
                showSupportingContent = true
            }
        }
    )

    TextFieldDialog(
        visible = showDialog,
        icon = leadingIcon,
        title = title,
        textFields = mapOf(
            "value" to TextFieldDialogData(
                keyboardType = KeyboardType.Decimal,
                initialValue = textValue,
                onValueChange = { it.takeIf { !onlyDecimal || it.checkDecimal() }  }
            )
        ),
        onDismiss = { showDialog = false },
        onConfirm = {
            textValue = it["value"].orEmpty()
            edit()
            showDialog = false
        }
    )

}

/**
 * A Composable function that creates an integer text field preference item with a conditional enable.
 * 
 * This variant provides the ability to enable/disable the preference based on the
 * value of another boolean preference.
 * 
 * @param modifier Modifier to be applied to the layout
 * @param title The title text for the integer preference
 * @param summary Composable function that displays the current integer value as a summary
 * @param key The Preferences.Key used to identify this preference in the DataStore
 * @param defaultValue The default integer value
 * @param onlyDecimal Whether to only allow decimal inputs
 * @param enabled Pair of Preferences.Key and default boolean value to enable/disable this preference
 * @param leadingIcon Optional icon to display before the title
 * @param showIcon Whether to show the leading icon
 */
@Composable
fun SectionScope.TextFieldPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Int) -> String) = { it.toString() },
    key: Preferences.Key<Int>,
    defaultValue: Int,
    onlyDecimal: Boolean = true,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingIcon: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember { dataStore.data }.collectAsState(initial = null)

    var checked = enabled.second
    prefs?.get(enabled.first)?.also { checked = it }

    TextFieldPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        onlyDecimal = onlyDecimal,
        enabled = checked,
        leadingIcon = leadingIcon,
        showIcon = showIcon,
    )
}