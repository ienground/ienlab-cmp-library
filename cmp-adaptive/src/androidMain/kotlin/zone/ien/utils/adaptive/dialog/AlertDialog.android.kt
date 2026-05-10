package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.M3AlertDialog

/**
 * 알림 다이얼로그 컴포저블 (안드로이드 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textDismiss 닫기 버튼 텍스트
 * @param styleDismiss 닫기 버튼 스타일
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @return 알림 다이얼로그 컴포저블 (안드로이드)
 */
@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: (() -> Unit)?
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textDismiss = textDismiss,
        onDismiss = onDismiss
    )
}

/**
 * 알림 다이얼로그 컴포저블 (안드로이드 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textDismiss 취소 버튼 텍스트
 * @param styleDismiss 취소 버튼 스타일
 * @param onDismiss 취소 버튼 클릭 시 실행할 함수
 * @param textConfirm 확인 버튼 텍스트
 * @param styleConfirm 확인 버튼 스타일
 * @param onConfirm 확인 버튼 클릭 시 실행할 함수
 * @param enabledConfirm 확인 버튼 활성화 여부
 * @return 알림 다이얼로그 컴포저블 (안드로이드)
 */
@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textDismiss: String,
    styleDismiss: UIAlertActionStyle,
    onDismiss: () -> Unit,
    textConfirm: String,
    styleConfirm: UIAlertActionStyle,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textDismiss = textDismiss,
        onDismiss = onDismiss,
        textConfirm = textConfirm,
        onConfirm = onConfirm,
        enabledConfirm = enabledConfirm
    )
}

/**
 * 알림 다이얼로그 컴포저블 (안드로이드 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textNeutral 중립 버튼 텍스트
 * @param styleNeutral 중립 버튼 스타일
 * @param onNeutral 중립 버튼 클릭 시 실행할 함수
 * @param enabledNeutral 중립 버튼 활성화 여부
 * @param textNegative 부정 버튼 텍스트
 * @param styleNegative 부정 버튼 스타일
 * @param onNegative 부정 버튼 클릭 시 실행할 함수
 * @param textPositive 긍정 버튼 텍스트
 * @param stylePositive 긍정 버튼 스타일
 * @param onPositive 긍정 버튼 클릭 시 실행할 함수
 * @param enabledPositive 긍정 버튼 활성화 여부
 * @return 알림 다이얼로그 컴포저블 (안드로이드)
 */
@Composable
actual fun AlertDialog(
    modifier: Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)?,
    title: String?,
    message: String?,
    textNeutral: String,
    styleNeutral: UIAlertActionStyle,
    onNeutral: () -> Unit,
    enabledNeutral: Boolean,
    textNegative: String,
    styleNegative: UIAlertActionStyle,
    onNegative: () -> Unit,
    textPositive: String,
    stylePositive: UIAlertActionStyle,
    onPositive: () -> Unit,
    enabledPositive: Boolean
) {
    M3AlertDialog(
        modifier = modifier,
        visible = visible,
        icon = icon,
        title = title,
        message = message,
        textNeutral = textNeutral,
        onNeutral = onNeutral,
        enabledNeutral = enabledNeutral,
        textNegative = textNegative,
        onNegative = onNegative,
        textPositive = textPositive,
        onPositive = onPositive,
        enabledPositive = enabledPositive
    )
}