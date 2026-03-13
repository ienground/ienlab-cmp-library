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