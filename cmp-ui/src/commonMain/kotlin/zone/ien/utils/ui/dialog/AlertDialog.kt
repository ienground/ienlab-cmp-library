package zone.ien.utils.ui.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

/**
 * M3BaseAlertDialog는 AlertDialog의 기본 구조를 정의하는 컴포저블로, 다이얼로그의 내용을 설정합니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param buttons 다이얼로그의 버튼을 나타내는 Composable
 */
@Composable
fun M3BaseAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    onDismiss: () -> Unit,
    buttons: @Composable RowScope.() -> Unit
) {
    if (visible) {
        BaseDialog(
            modifier = modifier,
            icon = icon,
            title = title,
            content = message?.let { {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            } },
            onCancel = onDismiss,
            buttons = { Row(modifier = it) { buttons() } }
        )
    }
}

/**
 * M3AlertDialog은 간단한 확인/취소 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textDismiss 취소 버튼의 텍스트
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.close),
    onDismiss: (() -> Unit)?,
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss ?: {},
        buttons = {
            Spacer(modifier = Modifier.weight(1f))

            onDismiss?.let {
                TextButton(
                    onClick = it,
                ) { Text(text = textDismiss) }
            }
        }
    )
}

/**
 * M3AlertDialog은 확인/취소 다이얼로그를 제공하는 컴포저블로, 확인 버튼이 있는 형태입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textDismiss 취소 버튼의 텍스트
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param textConfirm 확인 버튼의 텍스트
 * @param onConfirm 확인 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledConfirm 확인 버튼의 활성화 여부
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.cancel),
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onDismiss,
        buttons = {
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onDismiss,
            ) { Text(text = textDismiss) }

            TextButton(
                onClick = onConfirm,
                enabled = enabledConfirm
            ) { Text(text = textConfirm) }
        }
    )
}

/**
 * M3AlertDialog은 세 가지 버튼(중립, 부정, 긍정)을 포함한 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param icon 다이얼로그의 아이콘을 나타내는 Composable
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 내용
 * @param textNeutral 중립 버튼의 텍스트
 * @param onNeutral 중립 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledNeutral 중립 버튼의 활성화 여부
 * @param textNegative 부정 버튼의 텍스트
 * @param onNegative 부정 버튼을 누를 때 호출되는 콜백 함수
 * @param textPositive 긍정 버튼의 텍스트
 * @param onPositive 긍정 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledPositive 긍정 버튼의 활성화 여부
 */
@Composable
fun M3AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textNeutral: String = stringResource(Res.string.close),
    onNeutral: () -> Unit,
    enabledNeutral: Boolean = true,
    textNegative: String = stringResource(Res.string.cancel),
    onNegative: () -> Unit,
    textPositive: String = stringResource(Res.string.ok),
    onPositive: () -> Unit,
    enabledPositive: Boolean = true
) {
    M3BaseAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        onDismiss = onNegative,
        buttons = {
            TextButton(
                onClick = onNeutral,
                enabled = enabledNeutral
            ) { Text(text = textNeutral) }

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onNegative,
            ) { Text(text = textNegative) }

            TextButton(
                onClick = onPositive,
                enabled = enabledPositive
            ) { Text(text = textPositive) }
        }
    )
}