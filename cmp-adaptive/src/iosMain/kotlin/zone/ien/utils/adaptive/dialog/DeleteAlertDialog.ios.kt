package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.delete_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.ui.dialog.IenDialogButtonLayout

/**
 * 삭제 다이얼로그 컴포저블 (iOS 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onDismiss 취소 버튼 클릭 시 실행할 함수
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수
 * @param enabledConfirm 확인 버튼 활성화 여부
 * @return 삭제 다이얼로그 컴포저블 (iOS)
 */
@Composable
actual fun DeleteAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean,
    buttonLayout: IenDialogButtonLayout
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = stringResource(Res.string.delete_dialog_title),
        message = stringResource(Res.string.delete_dialog_content),
        onDismiss = onDismiss,
        styleConfirm = UIAlertActionStyle.Destructive,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm,
        buttonLayout = buttonLayout,
    )
}
