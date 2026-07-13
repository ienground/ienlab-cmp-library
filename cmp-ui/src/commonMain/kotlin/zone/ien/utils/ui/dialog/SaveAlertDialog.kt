package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.not_save
import zone.ien.utils.cmp_ui.generated.resources.save
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.save_dialog_title
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.icon.material.M3SystemIcons

/**
 * IenSaveAlertDialog은 데이터 저장 확인을 위한 다이얼로그 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param onCancel 다이얼로그를 취소할 때 호출되는 콜백 함수
 * @param onUnsave 저장하지 않고 닫을 때 호출되는 콜백 함수
 * @param enabledUnsave 저장하지 않고 닫기 버튼의 활성화 여부
 * @param onSave 저장 버튼을 누를 때 호출되는 콜백 함수
 * @param enabledSave 저장 버튼의 활성화 여부
 */
@Composable
fun IenSaveAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCancel: () -> Unit,
    onUnsave: () -> Unit,
    enabledUnsave: Boolean = true,
    onSave: () -> Unit,
    enabledSave: Boolean = true,
) {
    IenAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = M3SystemIcons.Save, contentDescription = null) },
        title = stringResource(Res.string.save_dialog_title),
        message = stringResource(Res.string.save_dialog_content),
        textNeutral = stringResource(Res.string.not_save),
        onNeutral = onUnsave,
        enabledNeutral = enabledUnsave,
        textNegative = stringResource(Res.string.cancel),
        onNegative = onCancel,
        textPositive = stringResource(Res.string.save),
        onPositive = onSave,
        enabledPositive = enabledSave
    )
}