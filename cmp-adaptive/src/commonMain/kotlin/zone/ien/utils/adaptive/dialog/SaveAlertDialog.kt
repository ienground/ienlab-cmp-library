package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenDialogButtonLayout

/**
 * 저장 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onCancel 취소 버튼 클릭 시 실행할 함수
 * @param onUnsave 미저장 버튼 클릭 시 실행할 함수
 * @param enabledUnsave 미저장 버튼 활성화 여부
 * @param onSave 저장 버튼 클릭 시 실행할 함수
 * @param enabledSave 저장 버튼 활성화 여부
 * @param buttonLayout 저장/취소/미저장 버튼의 배치 방향
 * @return 저장 다이얼로그 컴포저블
 */
@Composable
expect fun SaveAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onCancel: () -> Unit,
    onUnsave: () -> Unit,
    enabledUnsave: Boolean = true,
    onSave: () -> Unit,
    enabledSave: Boolean = true,
    buttonLayout: IenDialogButtonLayout = IenDialogButtonLayout.Vertical,
)
