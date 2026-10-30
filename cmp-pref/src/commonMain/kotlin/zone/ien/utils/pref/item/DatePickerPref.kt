package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.launch
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.dialog.DatePickerDialog
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.IenSectionLinkAdaptation
import zone.ien.utils.pref.LocalPrefsAdaptive
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.ui.dialog.IenDatePickerDialog
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionLinkDefault

/**
 * 날짜를 밀리초 단위 Long으로 저장하는 설정 항목입니다. 기본값과 저장값은 선택 연도 범위 안의 날짜여야 합니다.
 *
 * @param yearRange 선택 가능한 연도 범위입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SectionScope.DatePickerPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable (Long) -> String,
    key: Preferences.Key<Long>,
    defaultValue: Long,
    enabled: Boolean = true,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    yearRange: IntRange = DatePickerDefaults.YearRange,
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by rememberSaveable(key.name) { mutableStateOf(false) }
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val value = prefs?.get(key) ?: defaultValue

    TextPref(
        onClick = { if (enabled) showDialog = !showDialog },
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = summary(value),
        leadingContent = if (showIcon) leadingContent else null,
        trailingContent = trailingContent,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        adaptation = {
            cupertino {
                this.showSupportingContent = true
                this.isCaption = false
            }
            adaptation()
        },
    )

    PrefsDatePickerDialog(
        modifier = dialogModifier,
        yearRange = yearRange,
        visible = showDialog,
        initialSelectedDateMillis = value,
        title = title,
        onDismiss = { showDialog = false },
        onConfirm = { selectedValue ->
            coroutineScope.launch {
                try {
                    dataStore.edit { it[key] = selectedValue }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            showDialog = false
        }
    )
}

/**
 * 다른 불리언 설정값에 따라 활성화되는 날짜 선택 항목입니다. 저장 단위와 선택 옵션은 기본 오버로드와 같습니다.
 *
 * @param enabled 활성화를 제어하는 설정 키와 기본값입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SectionScope.DatePickerPref(
    modifier: Modifier = Modifier,
    title: String,
    summary: @Composable ((Long) -> String),
    key: Preferences.Key<Long>,
    defaultValue: Long,
    enabled: Pair<Preferences.Key<Boolean>, Boolean>,
    leadingContent: @Composable (() -> Unit)? = null,
    showIcon: Boolean = false,
    onClickLabel: String? = null,
    indication: Indication? = LocalIndication.current,
    interactionSource: MutableInteractionSource? = null,
    colors: IenSectionColors = IenSectionLinkDefault.colors(),
    trailingContent: @Composable () -> Unit = {},
    adaptation: AdaptationScope<HigSectionLinkAdaptation, IenSectionLinkAdaptation>.() -> Unit = {},
    dialogModifier: Modifier = Modifier,
    yearRange: IntRange = DatePickerDefaults.YearRange,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val checked = prefs?.get(enabled.first) ?: enabled.second

    DatePickerPref(
        modifier = modifier,
        title = title,
        summary = summary,
        key = key,
        defaultValue = defaultValue,
        enabled = checked,
        leadingContent = leadingContent,
        showIcon = showIcon,
        onClickLabel = onClickLabel,
        indication = indication,
        interactionSource = interactionSource,
        colors = colors,
        trailingContent = trailingContent,
        adaptation = adaptation,
        dialogModifier = dialogModifier,
        yearRange = yearRange,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrefsDatePickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialSelectedDateMillis: Long,
    yearRange: IntRange,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    if (LocalPrefsAdaptive.current) {
        DatePickerDialog(
            modifier = modifier, visible = visible, initialSelectedDateMillis = initialSelectedDateMillis,
            yearRange = yearRange, title = title, onDismiss = onDismiss, onConfirm = onConfirm,
        )
    } else {
        IenDatePickerDialog(
            modifier = modifier, visible = visible, initialSelectedDateMillis = initialSelectedDateMillis,
            yearRange = yearRange, title = title, onDismiss = onDismiss, onConfirm = onConfirm,
        )
    }
}
