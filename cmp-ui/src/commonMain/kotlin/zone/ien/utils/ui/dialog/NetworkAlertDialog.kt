package zone.ien.utils.ui.dialog

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_content
import zone.ien.utils.cmp_ui.generated.resources.network_dialog_title
import zone.ien.utils.cmp_ui.generated.resources.retry
import zone.ien.utils.icon.material.M3SystemIcons

/**
 * IenNetworkAlertDialog은 네트워크 연결이 되지 않았을 때 표시하는 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param onDismiss 다이얼로그를 닫기 위한 콜백 함수
 */
@Composable
fun IenNetworkAlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onDismiss: (() -> Unit)?,
) {
    IenAlertDialog(
        modifier = modifier,
        visible = visible,
        icon = { Icon(imageVector = M3SystemIcons.CloudOff, contentDescription = null) },
        title = stringResource(Res.string.network_dialog_title),
        message = stringResource(Res.string.network_dialog_content),
        textDismiss = stringResource(Res.string.close),
        onDismiss = onDismiss,
    )
}