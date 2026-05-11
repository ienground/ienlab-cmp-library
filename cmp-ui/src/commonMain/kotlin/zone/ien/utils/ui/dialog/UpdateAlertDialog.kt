package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.version_update_dialog_title
import zone.ien.utils.icon.material.M3SystemIcons

/**
 * M3UpdateAlertDialog은 앱 업데이트를 위한 다이얼로그 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param appName 앱의 이름
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 */
@Composable
fun M3UpdateAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    appName: String,
    onDismiss: () -> Unit,
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = M3SystemIcons.Update, contentDescription = null) },
        title = stringResource(Res.string.version_update_dialog_title),
        message = stringResource(Res.string.version_update_dialog_content, appName),
        onDismiss = onDismiss,
        textDismiss = updateAlertDismissText
    )
}

/**
 * updateAlertDismissText는 업데이트 알림 다이얼로그의 취소 버튼 텍스트를 반환합니다.
 * 이 값은 플랫폼 별로 다르게 정의됩니다 (Android와 iOS).
 */
internal expect val updateAlertDismissText: String