package zone.ien.utils.pref.item

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import zone.ien.hig.adaptive.AdaptationScope
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.section.SectionScope
import zone.ien.utils.adaptive.section.HigSectionLinkAdaptation
import zone.ien.utils.adaptive.section.IenSectionLinkAdaptation
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.pref.LocalPrefsDataStore
import zone.ien.utils.ui.dialog.IenConfirmDialog
import zone.ien.utils.ui.dialog.IenConfirmDialogCancelButton
import zone.ien.utils.ui.dialog.IenConfirmDialogConfirmButton
import zone.ien.utils.ui.dialog.IenConfirmDialogTitle
import zone.ien.utils.ui.interactive.IenDurationWheelPicker
import zone.ien.utils.ui.section.IenSectionColors
import zone.ien.utils.ui.section.IenSectionLinkDefault

/**
 * 기간을 총 초 단위 Long으로 저장하는 설정 항목입니다. 확인 시에만 저장하며 취소하면 이전 값을 유지합니다.
 * Adaptive 모드에서도 IEN 휠 다이얼로그를 사용합니다.
 *
 * @param defaultValue 음수가 아닌 정수 초입니다. 시간을 표시하면 maxHours 범위 안이어야 합니다.
 * @param showHours 시간 휠 표시 여부입니다.
 * @param showMinutes 분 휠 표시 여부입니다.
 * @param showSeconds 초 휠 표시 여부입니다. 적어도 하나의 휠을 표시해야 합니다.
 * @param maxHours 선택 가능한 최대 시간입니다 (0..9999).
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.DurationPickerPref(
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
    showHours: Boolean = true,
    showMinutes: Boolean = true,
    showSeconds: Boolean = true,
    maxHours: Int = 23,
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

    PrefsDurationPickerDialog(
        modifier = dialogModifier,
        showHours = showHours,
        showMinutes = showMinutes,
        showSeconds = showSeconds,
        maxHours = maxHours,
        visible = showDialog,
        initialSeconds = value,
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
 * 다른 불리언 설정값에 따라 활성화되는 기간 선택 항목입니다. 저장 단위와 선택 옵션은 기본 오버로드와 같습니다.
 *
 * @param enabled 활성화를 제어하는 설정 키와 기본값입니다.
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SectionScope.DurationPickerPref(
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
    showHours: Boolean = true,
    showMinutes: Boolean = true,
    showSeconds: Boolean = true,
    maxHours: Int = 23,
) {
    val dataStore = LocalPrefsDataStore.current
    val prefs by remember(dataStore) { dataStore.data }.collectAsState(initial = null)
    val checked = prefs?.get(enabled.first) ?: enabled.second

    DurationPickerPref(
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
        showHours = showHours,
        showMinutes = showMinutes,
        showSeconds = showSeconds,
        maxHours = maxHours,
    )
}

@Composable
private fun PrefsDurationPickerDialog(
    modifier: Modifier,
    visible: Boolean,
    initialSeconds: Long,
    showHours: Boolean,
    showMinutes: Boolean,
    showSeconds: Boolean,
    maxHours: Int,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Long) -> Unit,
) {
    if (visible) {
        var seconds by rememberSaveable(showHours, showMinutes, showSeconds, maxHours) {
            mutableStateOf(
                if (showHours) initialSeconds.coerceAtMost(maxHours * 3600L + 3599L) else initialSeconds
            )
        }
        IenConfirmDialog(
            modifier = modifier,
            visible = true,
            onClose = onDismiss,
            title = { IenConfirmDialogTitle(text = title) },
            description = {
                IenDurationWheelPicker(
                    value = KDuration.of(seconds, KFixedTimeUnit.Second),
                    onValueChange = { seconds = it.toSeconds() },
                    modifier = Modifier.fillMaxWidth(),
                    showHours = showHours,
                    showMinutes = showMinutes,
                    showSeconds = showSeconds,
                    maxHours = maxHours,
                )
            },
            cancelButton = {
                IenConfirmDialogCancelButton(text = stringResource(Res.string.cancel), onClick = onDismiss)
            },
            confirmButton = {
                IenConfirmDialogConfirmButton(
                    text = stringResource(Res.string.ok),
                    onClick = { onConfirm(seconds) },
                )
            },
        )
    }
}
