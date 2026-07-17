package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.dialog.IenAlertDialogDescription
import zone.ien.utils.ui.dialog.IenAlertDialogTitle
import zone.ien.utils.ui.dialog.IenConfirmDialogCancelButton
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.interactive.IenButton
import zone.ien.utils.ui.interactive.IenButtonDisplay
import zone.ien.utils.ui.interactive.IenButtonSize
import zone.ien.utils.ui.interactive.IenButtonState
import zone.ien.utils.ui.interactive.IenButtonVariant
import zone.ien.utils.ui.primitives.IenText
import zone.ien.utils.ui.utils.TextFieldDialogData

/**
 * IenBaseTextFieldDialog은 텍스트 필드 다이얼로그의 기본 구조를 정의하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param textFields 다이얼로그에 표시할 텍스트 필드들을 나타내는 Composable
 * @param buttons 다이얼로그의 버튼을 나타내는 Composable
 */
@Composable
fun IenBaseTextFieldDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    onDismiss: () -> Unit,
    textFields: @Composable ColumnScope.() -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    IenDialogFrame(
        visible = visible,
        onDismiss = onDismiss,
        modifier = modifier,
    ) {
        if (icon != null || title != null) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
            ) {
                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides IenTheme.colors.brand) {
                        Box(contentAlignment = Alignment.Center) {
                            it()
                        }
                    }
                }
                title?.let {
                    IenAlertDialogTitle(text = it)
                }
            }
        }
        message?.let {
            IenAlertDialogDescription(text = it)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(IenTheme.spacing.sm),
        ) {
            textFields()
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(IenTheme.spacing.xs),
        ) {
            buttons()
        }
    }
}

/**
 * IenTextFieldDialog은 사용자 입력을 받기 위한 텍스트 필드 다이얼로그 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textFields 입력 필드들의 데이터 정의
 * @param textDismiss 취소 버튼의 텍스트
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param textConfirm 확인 버튼의 텍스트
 * @param onConfirm 입력 데이터를 처리하기 위한 콜백 함수
 */
@Composable
fun IenTextFieldDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textFields: Map<String, TextFieldDialogData> = mapOf(),
    textDismiss: String = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    onConfirm: (Map<String, String>) -> Unit,
) {
    val textStates = remember(visible, textFields.keys) {
        mutableStateMapOf<String, String>().apply {
            textFields.forEach { (key, data) ->
                put(key, data.initialValue)
            }
        }
    }

    val enabledConfirm by derivedStateOf {
        textFields.all { (key, field) ->
            val text = textStates[key] ?: ""
            field.valid(text)
        }
    }

    IenBaseTextFieldDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss,
        textFields = {
            textFields.forEach { (key, field) ->
                if (field.keyboardType in listOf(KeyboardType.Password, KeyboardType.NumberPassword)) {
                    val passwordState = remember { TextFieldState(initialText = textStates[key] ?: "") }

                    LaunchedEffect(passwordState.text) {
                        textStates[key] = passwordState.text.toString()
                    }

                    SecureTextField(
                        state = passwordState,
                        placeholder = { Text(text = field.placeholder) },
                        prefix = field.prefix?.let { { Text(text = it) } },
                        suffix = field.suffix?.let { { Text(text = it) } },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = field.keyboardType,
                            imeAction = field.imeAction
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    TextField(
                        value = textStates[key] ?: "",
                        onValueChange = {
                            field.onValueChange(it)?.let { textStates[key] = it }
                        },
                        placeholder = { Text(text = field.placeholder) },
                        prefix = field.prefix?.let { { Text(text = it) } },
                        suffix = field.suffix?.let { { Text(text = it) } },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = field.keyboardType,
                            imeAction = field.imeAction
                        ),
                        maxLines = field.maxLines,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        buttons = {
            Box(modifier = Modifier.weight(1f)) {
                IenConfirmDialogCancelButton(
                    text = textDismiss,
                    onClick = onDismiss,
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                IenButton(
                    onClick = { onConfirm(textStates.mapValues { it.value.trim() }) },
                    modifier = Modifier.fillMaxWidth(),
                    size = IenButtonSize.Large,
                    variant = IenButtonVariant.Fill,
                    tone = IenSemanticTone.Brand,
                    state = IenButtonState(enabled = enabledConfirm),
                    display = IenButtonDisplay.Block,
                ) {
                    IenText(textConfirm)
                }
            }
        }
    )
}
