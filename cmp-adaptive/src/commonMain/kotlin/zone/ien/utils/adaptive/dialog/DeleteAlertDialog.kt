package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 삭제 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onDismiss 취소 버튼 클릭 시 실행할 함수
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수
 * @param enabledConfirm 확인 버튼 활성화 여부
 * @return 삭제 다이얼로그 컴포저블
 */
@Composable
expect fun DeleteAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
)