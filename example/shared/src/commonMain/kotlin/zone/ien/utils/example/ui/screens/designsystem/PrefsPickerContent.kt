package zone.ien.utils.example.ui.screens.designsystem

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import com.sunnychung.lib.multiplatform.kdatetime.KZoneOffset
import com.sunnychung.lib.multiplatform.kdatetime.serializer.KInstantAsLong
import com.sunnychung.lib.multiplatform.kdatetime.toKZonedDateTime
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import zone.ien.utils.example.di.KoinKey.DEFAULT_DATASTORE
import zone.ien.utils.pref.PrefsGroup
import zone.ien.utils.pref.PrefsScreen
import zone.ien.utils.pref.PrefsScreenType
import zone.ien.utils.pref.item.DatePickerPref
import zone.ien.utils.pref.item.DurationPickerPref
import zone.ien.utils.pref.item.TimePickerPref

@Composable
internal fun PrefsPickerContent(controls: Map<String, String>) {
    val dataStore: DataStore<Preferences> = koinInject(named(DEFAULT_DATASTORE))
    val enabled = controls["disabled"] != "true"
    val showHours = controls["showHours"]?.toBooleanStrictOrNull() ?: true
    val showMinutes = controls["showMinutes"]?.toBooleanStrictOrNull() ?: true
    val showSeconds = controls["showSeconds"]?.toBooleanStrictOrNull() ?: true
    val maxHours = (controls["maxHours"]?.toIntOrNull() ?: 48).coerceIn(0, 9999)

    PrefsScreen(
        dataStore = dataStore,
        type = PrefsScreenType.IEN,
        fullHeight = false,
        scrollState = null,
    ) {
        PrefsGroup {
            DatePickerPref(
                title = "날짜",
                summary = {
                    val date = KInstantAsLong(it).atZoneOffset(KZoneOffset.UTC).toKZonedDateTime().datePart()
                    "${date.year}-${date.month.toString().padStart(2, '0')}-${date.day.toString().padStart(2, '0')}"
                },
                key = longPreferencesKey("design_system_picker_date"),
                defaultValue = 1792800000000L,
                enabled = enabled,
            )
            TimePickerPref(
                title = "시각",
                summary = { "${it / 60}시 ${it % 60}분" },
                key = intPreferencesKey("design_system_picker_time"),
                defaultValue = 9 * 60,
                enabled = enabled,
                is24Hour = controls["is24Hour"]?.toBooleanStrictOrNull() ?: true,
            )
            DurationPickerPref(
                title = "기간",
                summary = { "${it / 3600}시간 ${it / 60 % 60}분 ${it % 60}초" },
                key = longPreferencesKey("design_system_picker_duration"),
                defaultValue = 300L,
                enabled = enabled && (showHours || showMinutes || showSeconds),
                showHours = showHours,
                showMinutes = showMinutes,
                showSeconds = showSeconds,
                maxHours = maxHours,
            )
        }
        if (!showHours && !showMinutes && !showSeconds) {
            Text("기간 필드를 하나 이상 선택하세요.")
        }
    }
}
