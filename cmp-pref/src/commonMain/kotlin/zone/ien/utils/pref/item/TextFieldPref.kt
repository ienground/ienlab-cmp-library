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
 * 다이얼로그를 통해 텍스트를 입력받는 설정 항목을 생성하는 Composable 함수입니다.
 * 
 * 이 설정 항목은 현재 텍스트 값을 표시하며, 클릭 시 사용자가 텍스트를 수정할 수 있는 다이얼로그를 엽니다.
 * 문자열 및 정수 값을 모두 처리할 수 있습니다.
 * 
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 텍스트 설정의 제목 텍스트
 * @param summary 현재 값을 요약으로 표시하는 Composable 함수
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 텍스트 필드의 기본값
 * @param enabled 설정의 활성화 여부
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
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
            "value" to TextFieldDialogData(
                initialValue = textValue
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
 * 조건부 활성화 기능이 포함된 텍스트 필드 설정 항목을 생성하는 Composable 함수입니다.
 *
 * 이 변체는 다른 불리언 설정 값에 따라 이 설정을 활성화 또는 비활성화할 수 있는 기능을 제공합니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 텍스트 설정의 제목 텍스트
 * @param summary 현재 값을 요약으로 표시하는 Composable 함수
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 텍스트 필드의 기본값
 * @param enabled 이 설정을 활성화/비활성화하기 위한 Preferences.Key와 기본 불리언 값의 쌍
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
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
 * 정수형 텍스트 필드 설정 항목을 생성하는 Composable 함수입니다.
 * 
 * 이 설정 항목은 정수 값을 표시하며 다이얼로그에서 편집할 수 있도록 합니다.
 * 십진수만 입력할 수 있도록 유효성 검사를 지원합니다.
 * 
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 정수 설정의 제목 텍스트
 * @param summary 현재 정수 값을 요약으로 표시하는 Composable 함수
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 기본 정수 값
 * @param onlyDecimal 십진수 입력만 허용할지 여부
 * @param enabled 설정의 활성화 여부
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
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
 * 조건부 활성화 기능이 포함된 정수형 텍스트 필드 설정 항목을 생성하는 Composable 함수입니다.
 *
 * 이 변체는 다른 불리언 설정 값에 따라 이 설정을 활성화 또는 비활성화할 수 있는 기능을 제공합니다.
 *
 * @param modifier 레이아웃에 적용할 Modifier
 * @param title 정수 설정의 제목 텍스트
 * @param summary 현재 정수 값을 요약으로 표시하는 Composable 함수
 * @param key DataStore에서 이 설정을 식별하는 데 사용되는 Preferences.Key
 * @param defaultValue 기본 정수 값
 * @param onlyDecimal 십진수 입력만 허용할지 여부
 * @param enabled 이 설정을 활성화/비활성화하기 위한 Preferences.Key와 기본 불리언 값의 쌍
 * @param leadingIcon 제목 앞에 표시할 선택적 아이콘
 * @param showIcon 선행 아이콘을 표시할지 여부
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