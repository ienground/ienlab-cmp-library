package zone.ien.utils.ui.feedback

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenSemanticTone
import zone.ien.utils.ui.dialog.IenAlertDialog
import zone.ien.utils.ui.dialog.IenConfirmDialog
import zone.ien.utils.ui.dialog.IenConfirmDialogCancelButton
import zone.ien.utils.ui.dialog.IenConfirmDialogConfirmButton
import zone.ien.utils.ui.dialog.IenConfirmDialogDescription
import zone.ien.utils.ui.dialog.IenConfirmDialogTitle

/**
 * 다이얼로그([IenDialog]) 내의 버튼 동작을 구성하는 데이터 모델입니다.
 *
 * @property text 버튼에 표시할 문구
 * @property onClick 버튼을 클릭했을 때 실행할 콜백 함수
 * @property tone 버튼의 시맨틱 톤 설정 ([IenSemanticTone])
 * @property enabled 버튼 활성화 여부
 */
@Immutable
data class IenDialogAction(
    val text: String,
    val onClick: () -> Unit,
    val tone: IenSemanticTone = IenSemanticTone.Brand,
    val enabled: Boolean = true,
)

/**
 * 사용자에게 알림 또는 확인 작업을 수행할 때 표시되는 다이얼로그 컴포저블입니다.
 *
 * [dismiss] 액션 여부에 따라 알림창([IenAlertDialog]) 또는 확인창([IenConfirmDialog])으로 렌더링됩니다.
 *
 * @param visible 다이얼로그 표시 여부
 * @param onDismissRequest 다이얼로그 외부 클릭 또는 뒤로 가기 등으로 닫힐 때 호출되는 콜백
 * @param title 다이얼로그의 제목
 * @param message 다이얼로그의 본문 메시지
 * @param confirm 확인/긍정 버튼 액션 ([IenDialogAction])
 * @param modifier 다이얼로그 레이아웃에 적용할 [Modifier]
 * @param dismiss 취소/부정 버튼 액션 ([IenDialogAction]). 생략할 경우 단일 버튼 알림 다이얼로그로 동작합니다.
 */
@Composable
fun IenDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    title: String,
    message: String,
    confirm: IenDialogAction,
    modifier: Modifier = Modifier,
    dismiss: IenDialogAction? = null,
) {
    if (dismiss == null) {
        IenAlertDialog(
            visible = visible,
            title = title,
            message = message,
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            confirmText = confirm.text,
            onConfirmClick = confirm.onClick,
            tone = confirm.tone,
            enabledConfirm = confirm.enabled,
        )
    } else {
        IenConfirmDialog(
            visible = visible,
            onClose = onDismissRequest,
            modifier = modifier,
            title = {
                IenConfirmDialogTitle(text = title)
            },
            description = {
                IenConfirmDialogDescription(text = message)
            },
            cancelButton = {
                IenConfirmDialogCancelButton(
                    text = dismiss.text,
                    onClick = dismiss.onClick,
                    tone = dismiss.tone,
                    enabled = dismiss.enabled,
                )
            },
            confirmButton = {
                IenConfirmDialogConfirmButton(
                    text = confirm.text,
                    onClick = confirm.onClick,
                    tone = confirm.tone,
                    enabled = confirm.enabled,
                )
            },
        )
    }
}
