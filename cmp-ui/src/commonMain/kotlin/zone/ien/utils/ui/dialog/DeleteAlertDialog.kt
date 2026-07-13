package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.icon.material.M3SystemIcons

/**
 * IenDeleteAlertDialog은 항목 삭제 확인을 위한 다이얼로그 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 * @param onConfirm 삭제 확인 시 호출되는 콜백 함수
 * @param enabledConfirm 확인 버튼의 활성화 여부
 */
@Composable
fun IenDeleteAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
) {
    IenAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = M3SystemIcons.Delete, contentDescription = null) },
        title = stringResource(Res.string.delete_dialog_title),
        message = stringResource(Res.string.delete_dialog_content),
        onDismiss = onDismiss,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm,
        isDestructive = true
    )
}