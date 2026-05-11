package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 네트워크 상태 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @return 네트워크 상태 다이얼로그 컴포저블
 */
@Composable
expect fun NetworkAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
)