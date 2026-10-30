package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.sunnychung.lib.multiplatform.kdatetime.KDuration
import com.sunnychung.lib.multiplatform.kdatetime.KFixedTimeUnit
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import zone.ien.utils.ui.dialog.IenConfirmDialog
import zone.ien.utils.ui.dialog.IenConfirmDialogCancelButton
import zone.ien.utils.ui.dialog.IenConfirmDialogConfirmButton
import zone.ien.utils.ui.dialog.IenConfirmDialogTitle
import zone.ien.utils.ui.dialog.IenDatePickerDialog
import zone.ien.utils.ui.dialog.IenTimePickerDialog
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenDurationWheelPicker
import androidx.compose.material3.Text
import zone.ien.utils.ui.section.IenSection
import zone.ien.utils.ui.section.link

@Composable
internal fun PrefsPickerContent(controls: Map<String, String> = emptyMap()) {
    val enabled = controls["disabled"] != "true"
    val is24Hour = controls["is24Hour"]?.toBooleanStrictOrNull() ?: true
    val showHours = controls["showHours"]?.toBooleanStrictOrNull() ?: true
    val showMinutes = controls["showMinutes"]?.toBooleanStrictOrNull() ?: true
    val showSeconds = controls["showSeconds"]?.toBooleanStrictOrNull() ?: true
    val maxHours = (controls["maxHours"]?.toIntOrNull() ?: 48).coerceIn(0, 9999)
    var dateMillis by remember { mutableStateOf(1792800000000L) }
    var timeMinutes by remember { mutableStateOf(9 * 60) }
    var durationSeconds by remember { mutableStateOf(300L) }
    var activePicker by remember { mutableStateOf<String?>(null) }
    val date = KInstantAsLong(dateMillis).atZoneOffset(KZoneOffset.UTC).toKZonedDateTime().datePart()

    Column(verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.md)) {
        Text(
            "Android/iOS의 DatePickerPref·TimePickerPref·DurationPickerPref 동작을 보여 주는 웹 미리보기입니다. 설정값은 저장되지 않습니다.",
            style = IenTheme.typography.body2,
            color = IenTheme.colors.textSecondary,
        )
        IenSection {
            link(
                title = { Text("날짜") },
                caption = { Text("${date.year}-${date.month.toString().padStart(2, '0')}-${date.day.toString().padStart(2, '0')}") },
                enabled = enabled,
                onClick = { activePicker = "Date" },
            )
            link(
                title = { Text("시각") },
                caption = { Text("${timeMinutes / 60}시 ${timeMinutes % 60}분") },
                enabled = enabled,
                onClick = { activePicker = "Time" },
            )
            link(
                title = { Text("기간") },
                caption = { Text("${durationSeconds / 3600}시간 ${durationSeconds / 60 % 60}분 ${durationSeconds % 60}초") },
                enabled = enabled && (showHours || showMinutes || showSeconds),
                onClick = { activePicker = "Duration" },
            )
        }
        if (!showHours && !showMinutes && !showSeconds) {
            Text("기간 필드를 하나 이상 선택하세요.", color = IenTheme.colors.textSecondary)
        }
    }
    IenDatePickerDialog(
        visible = activePicker == "Date",
        initialSelectedDateMillis = dateMillis,
        title = "날짜",
        onDismiss = { activePicker = null },
        onConfirm = { dateMillis = it; activePicker = null },
    )
    IenTimePickerDialog(
        visible = activePicker == "Time",
        initialHour = timeMinutes / 60,
        initialMinute = timeMinutes % 60,
        is24Hour = is24Hour,
        title = "시각",
        onDismiss = { activePicker = null },
        onConfirm = { hour, minute -> timeMinutes = hour * 60 + minute; activePicker = null },
    )
    if (activePicker == "Duration" && (showHours || showMinutes || showSeconds)) {
        var draftSeconds by remember(maxHours, showHours, showMinutes, showSeconds) {
            mutableStateOf(if (showHours) durationSeconds.coerceAtMost(maxHours * 3600L + 3599) else durationSeconds)
        }
        IenConfirmDialog(
            visible = true,
            onClose = { activePicker = null },
            title = { IenConfirmDialogTitle("기간") },
            description = {
                IenDurationWheelPicker(
                    value = KDuration.of(draftSeconds, KFixedTimeUnit.Second),
                    onValueChange = { draftSeconds = it.toSeconds() },
                    modifier = Modifier.fillMaxWidth(),
                    showHours = showHours,
                    showMinutes = showMinutes,
                    showSeconds = showSeconds,
                    maxHours = maxHours,
                )
            },
            cancelButton = {
                IenConfirmDialogCancelButton(text = "취소", onClick = { activePicker = null })
            },
            confirmButton = {
                IenConfirmDialogConfirmButton(
                    text = "확인",
                    onClick = { durationSeconds = draftSeconds; activePicker = null },
                )
            },
        )
    }
}
