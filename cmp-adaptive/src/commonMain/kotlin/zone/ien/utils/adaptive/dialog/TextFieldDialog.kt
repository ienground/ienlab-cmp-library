package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.ok
import zone.ien.utils.ui.utils.TextFieldDialogData

/**
 * 텍스트 필드 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textFields 텍스트 필드 데이터 맵
 * @param textDismiss 취소 버튼 텍스트
 * @param styleDismiss 취소 버튼 스타일
 * @param onDismiss 취소 버튼 클릭 시 실행할 함수
 * @param textConfirm 확인 버튼 텍스트
 * @param styleConfirm 확인 버튼 스타일
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수 (전달된 텍스트 필드 값 맵)
 * @return 텍스트 필드 다이얼로그 컴포저블
 */
@Composable
expect fun TextFieldDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textFields: Map<String, TextFieldDialogData> = mapOf(),
    textDismiss: String = stringResource(Res.string.cancel),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    styleConfirm: UIAlertActionStyle = UIAlertActionStyle.Default,
    onConfirm: (Map<String, String>) -> Unit,
)