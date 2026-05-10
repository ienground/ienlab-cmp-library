package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_title
import zone.ien.utils.cmp_ui.generated.resources.retry

/**
 * 네트워크 상태 다이얼로그 컴포저블 (iOS 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @return 네트워크 상태 다이얼로그 컴포저블 (iOS)
 */
@Composable
actual fun NetworkAlertDialog(
    modifier: Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
) {
    AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = null,
        title = stringResource(Res.string.network_dialog_title),
        message = stringResource(Res.string.network_dialog_content),
        textDismiss = stringResource(Res.string.close),
        onDismiss = onDismiss,
    )
}