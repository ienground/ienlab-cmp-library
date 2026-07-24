package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.not_save
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.ui.dialog.IenDialogButtonLayout

/**
 * 저장 다이얼로그 컴포저블 (iOS 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onCancel 취소 버튼 클릭 시 실행할 함수
 * @param onUnsave 미저장 버튼 클릭 시 실행할 함수
 * @param enabledUnsave 미저장 버튼 활성화 여부
 * @param onSave 저장 버튼 클릭 시 실행할 함수
 * @param enabledSave 저장 버튼 활성화 여부
 * @return 저장 다이얼로그 컴포저블 (iOS)
 */
@Composable
actual fun SaveAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onCancel: () -> Unit,
    onUnsave: () -> Unit,
    enabledUnsave: Boolean,
    onSave: () -> Unit,
    enabledSave: Boolean,
    buttonLayout: IenDialogButtonLayout
) {
    AlertDialog(
        visible = visible,
        icon = null,
        title = stringResource(Res.string.save_dialog_title),
        message = stringResource(Res.string.save_dialog_content),
        textNeutral = stringResource(Res.string.not_save),
        styleNeutral = UIAlertActionStyle.Destructive,
        onNeutral = onUnsave,
        enabledNeutral = enabledUnsave,
        textNegative = stringResource(Res.string.cancel),
        styleNegative = UIAlertActionStyle.Cancel,
        onNegative = onCancel,
        textPositive = stringResource(Res.string.save),
        stylePositive = UIAlertActionStyle.Default,
        onPositive = onSave,
        enabledPositive = enabledSave,
        buttonLayout = buttonLayout,
    )
}
