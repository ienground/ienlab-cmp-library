package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertActionStyleDestructive
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import zone.ien.utils.ui.dialog.IenDialogButtonLayout

/**
 * 알림 다이얼로그 버튼 스타일을 UIKit 스타일로 변환하는 함수
 *
 * @param this UIAlertActionStyle
 * @return UIKit 버튼 스타일
 */
internal fun UIAlertActionStyle.toStyle() = when (this) {
    UIAlertActionStyle.Cancel -> UIAlertActionStyleCancel
    UIAlertActionStyle.Default -> UIAlertActionStyleDefault
    UIAlertActionStyle.Destructive -> UIAlertActionStyleDestructive
}

/**
 * 알림 다이얼로그의 기본 기반 컴포저블 (iOS 플랫폼 구현)
 *
 * @param visible 다이얼로그 표시 여부
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param buttons 버튼 설정 함수
 * @return 알림 다이얼로그 기본 기반 컴포저블 (iOS)
 */
@Composable
fun HigBaseAlertDialog(
    visible: Boolean,
    title: String?,
    message: String?,
    buttons: (UIAlertController) -> Unit
) {
    val viewController = LocalUIViewController.current
    var alertRef by remember { mutableStateOf<UIAlertController?>(null) }

    LaunchedEffect(visible) {
        if (visible) {
            val alert = UIAlertController.alertControllerWithTitle(
                title = title,
                message = message,
                preferredStyle = UIAlertControllerStyleAlert
            )

            buttons(alert)

            alertRef = alert
            viewController.presentViewController(alert, animated = true, completion = null)
        } else {
            alertRef?.dismissViewControllerAnimated(true, null)
            alertRef = null
        }
    }

}

/**
 * 알림 다이얼로그 컴포저블 (iOS 플랫폼 구현 - 단일 버튼)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textDismiss 닫기 버튼 텍스트
 * @param styleDismiss 닫기 버튼 스타일
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @return 알림 다이얼로그 컴포저블 (iOS)
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
    HigBaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        onDismiss?.let { callback ->
            val dismissAction = UIAlertAction.actionWithTitle(
                title = textDismiss,
                style = styleDismiss.toStyle(),
                handler = {
                    callback()
                }
            )
            alertController.addAction(dismissAction)
        }

    }
}

/**
 * 알림 다이얼로그 컴포저블 (iOS 플랫폼 구현 - 확인/취소 버튼)
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
 * @param buttonLayout 버튼 배치 방향 (iOS UIKit UIAlertController 자체 레이아웃을 사용하므로 생략됨)
 * @return 알림 다이얼로그 컴포저블 (iOS)
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
    enabledConfirm: Boolean,
    buttonLayout: IenDialogButtonLayout
) {
    HigBaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        val dismissAction = UIAlertAction.actionWithTitle(
            title = textDismiss,
            style = styleDismiss.toStyle(),
            handler = {
                onDismiss()
            }
        )
        val confirmAction = UIAlertAction.actionWithTitle(
            title = textConfirm,
            style = styleConfirm.toStyle(),
            handler = {
                onConfirm()
            }
        ).apply {
            setEnabled(enabledConfirm)
        }

        alertController.addAction(dismissAction)
        alertController.addAction(confirmAction)

        alertController.preferredAction = confirmAction
    }
}

/**
 * 알림 다이얼로그 컴포저블 (iOS 플랫폼 구현 - 긍정/부정/중립 버튼)
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
 * @return 알림 다이얼로그 컴포저블 (iOS)
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
    enabledPositive: Boolean,
    buttonLayout: IenDialogButtonLayout
) {
    HigBaseAlertDialog(
        visible = visible,
        title = title,
        message = message
    ) { alertController ->
        val neutralAction = UIAlertAction.actionWithTitle(
            title = textNeutral,
            style = styleNeutral.toStyle(),
            handler = {
                onNeutral()
            }
        ).apply {
            setEnabled(enabledNeutral)
        }
        val negativeAction = UIAlertAction.actionWithTitle(
            title = textNegative,
            style = styleNegative.toStyle(),
            handler = {
                onNegative()
            }
        )
        val positiveAction = UIAlertAction.actionWithTitle(
            title = textPositive,
            style = stylePositive.toStyle(),
            handler = {
                onPositive()
            }
        ).apply {
            setEnabled(enabledPositive)
        }

        alertController.addAction(neutralAction)
        alertController.addAction(negativeAction)
        alertController.addAction(positiveAction)

        alertController.preferredAction = positiveAction
    }
}
