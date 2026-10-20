package zone.ien.utils.example.ui.screens.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import zone.ien.utils.adaptive.component.AdaptiveSwitch
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.adaptive.Theme
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.utils.adaptive.component.AdaptiveBackButton
import zone.ien.utils.adaptive.screen.AdaptiveTopAppBarScaffold
import zone.ien.utils.adaptive.theme.IenAdaptiveTheme
import zone.ien.utils.example.di.KoinKey.DEFAULT_DATASTORE
import zone.ien.utils.example.isIos
import zone.ien.utils.icon.IconData
import zone.ien.utils.icon.SystemIcons
import zone.ien.utils.icon.material.rounded.Delete
import zone.ien.utils.pref.PrefsGroup
import zone.ien.utils.pref.PrefsScreen
import zone.ien.utils.pref.PrefsScreenType
import zone.ien.utils.pref.item.DropdownPref
import zone.ien.utils.pref.item.SwitchPref
import zone.ien.utils.pref.item.TextFieldPref
import zone.ien.utils.pref.item.TextPref
import zone.ien.utils.ui.interactive.IenSwitch
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.primitives.IenIcon
import zone.ien.utils.ui.screen.IenScaffoldContentEdge
import zone.ien.utils.utils.sendEmail
import zone.ien.utils.example.ui.keyboardDismissAction

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    val backdrop = rememberDefaultBackdrop()
    val dataStore: DataStore<Preferences> = koinInject(named(DEFAULT_DATASTORE))
    var isMaterialTheme by remember { mutableStateOf(!isIos) }
    var useAdaptivePrefs by remember { mutableStateOf(true) }
    var textFieldValue by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    IenAdaptiveTheme(
        target = if (isMaterialTheme) Theme.Material3 else Theme.Cupertino
    ) {
        AdaptiveTopAppBarScaffold(
            navigationIcon = { AdaptiveBackButton(backdrop = backdrop) { navigateBack() } },
            contentEdge = IenScaffoldContentEdge(
                scrollableState = scrollState,
            ),
            actions = {
                Text("Adaptive")
                IenSwitch(
                    checked = useAdaptivePrefs,
                    onCheckedChange = { useAdaptivePrefs = it },
                )
                AdaptiveSwitch(
                    checked = isMaterialTheme,
                    onCheckedChange = { isMaterialTheme = it },
                )
            },
            modifier = modifier
        ) { pv, title ->
            PrefsScreen(
                dataStore = dataStore,
                type = if (useAdaptivePrefs) PrefsScreenType.Adaptive else PrefsScreenType.IEN,
                title = title,
                scrollState = scrollState,
                backdrop = backdrop,
                modifier = Modifier.padding(pv)
            ) {
                PrefsGroup(
                    title = { Text(text = "Pref Group") }
                ) {
                    DropdownPref(
                        title = "화면 모드",
                        key = stringPreferencesKey("display_mode"),
                        defaultValue = "system",
                        itemsWithLabels = mapOf(
                            "system" to "시스템 따름",
                            "light" to "라이트",
                            "dark" to "다크",
                        ),
                    )
                    SwitchPref(
                        title = "Hi",
                        summaryOn = "this is on",
                        summaryOff = "this is off",
                        defaultValue = true,
                        key = booleanPreferencesKey("test"),
                        onTrackContent = { Text("켜짐") },
                        offTrackContent = { Text("꺼짐") },
                        adaptation = { cupertino { showSupportingContent = true } },
                    )
                    TextPref(
                        title = "Title",
                        summary = "summary",
                        adaptation = { cupertino { showSupportingContent = true } }
                    )
                    TextPref(
                        title = "Title OnClick",
                        summary = "summary!!",
                        onClick = { sendEmail("my@ien.zone", "Hello", "World") },
                        adaptation = {
                            cupertino {
//                                isCaption = false
                                isCaption = true
                                showSupportingContent = true
                            }
                        }
                    )
                }
                PrefsGroup(
                    title = { Text(text = "Pref Group") }
                ) {
                    TextPref(
                        title = "Title OnClick",
                        summary = "summary!!",
                        onClick = {},
                        enabled = false,
                        adaptation = {
                            cupertino {
                                isCaption = false
                                showSupportingContent = false
                            }
                        }
                    )
                    IenTextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .keyboardDismissAction(),
                    )
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = {
                            IenIcon(
                                icon = IconData.Vector(SystemIcons.Delete),
                                contentDescription = null
                            )
                        },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        key = stringPreferencesKey("number"),
                        defaultValue = "!hi"
                    )
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = {
                            IenIcon(
                                icon = IconData.Vector(SystemIcons.Delete),
                                contentDescription = null
                            )
                        },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        key = intPreferencesKey("real_num"),
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        defaultValue = 1254
                    )
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = {
                            IenIcon(
                                icon = IconData.Vector(SystemIcons.Delete),
                                contentDescription = null
                            )
                        },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        key = intPreferencesKey("real_num"),
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        defaultValue = 1254
                    )
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = {
                            IenIcon(
                                icon = IconData.Vector(SystemIcons.Delete),
                                contentDescription = null
                            )
                        },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        key = intPreferencesKey("real_num"),
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        defaultValue = 1254
                    )
                }
                PrefsGroup(
                    title = { Text(text = "Pref Group") }
                ) {
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = { IenIcon(icon = IconData.Vector(SystemIcons.Delete), contentDescription = null) },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        key = intPreferencesKey("real_num"),
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        defaultValue = 1254
                    )
                    TextFieldPref(
                        dialogModifier = Modifier.keyboardDismissAction(),
                        leadingContent = { IenIcon(icon = IconData.Vector(SystemIcons.Delete), contentDescription = null) },
                        title = "Title OnClick",
                        summary = { "내 숫자는 ${it}" },
                        key = intPreferencesKey("real_num"),
                        enabled = Pair(booleanPreferencesKey("test"), true),
                        defaultValue = 1254
                    )
                }
            }
        }
    }
}
