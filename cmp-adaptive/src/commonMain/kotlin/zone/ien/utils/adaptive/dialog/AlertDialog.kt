package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.cancel
import zone.ien.utils.cmp_ui.generated.resources.close
import zone.ien.utils.cmp_ui.generated.resources.ok
import org.jetbrains.compose.resources.stringResource

/**
 * 알림 다이얼로그 스타일 열거형
 *
 * @property Default 기본 스타일
 * @property Cancel 취소 스타일
 * @property Destructive 파괴적 스타일 (예: 삭제 등의 위험한 작업)
 */
enum class UIAlertActionStyle {
    Default, Cancel, Destructive
}

/**
 * 단일 버튼 알림 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param icon 다이얼로그에 표시될 아이콘
 * @param title 다이얼로그 제목
 * @param message 다이얼로그 메시지
 * @param textDismiss 닫기 버튼 텍스트
 * @param styleDismiss 닫기 버튼 스타일
 * @param onDismiss 닫기 버튼 클릭 시 실행할 함수
 * @return 단일 버튼 알림 다이얼로그 컴포저블
 */
@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.close),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: (() -> Unit)?,
)

/**
 * 확인/취소 버튼 알림 다이얼로그 컴포저블
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
 * @return 확인/취소 버튼 알림 다이얼로그 컴포저블
 */
@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textDismiss: String = stringResource(Res.string.cancel),
    styleDismiss: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onDismiss: () -> Unit,
    textConfirm: String = stringResource(Res.string.ok),
    styleConfirm: UIAlertActionStyle = UIAlertActionStyle.Default,
    onConfirm: () -> Unit,
    enabledConfirm: Boolean = true
)

/**
 * 긍정/부정/중립 버튼 알림 다이얼로그 컴포저블
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
 * @return 긍정/부정/중립 버튼 알림 다이얼로그 컴포저블
 */
@Composable
expect fun AlertDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    icon: @Composable (() -> Unit)? = null,
    title: String?,
    message: String? = null,
    textNeutral: String = stringResource(Res.string.close),
    styleNeutral: UIAlertActionStyle = UIAlertActionStyle.Default,
    onNeutral: () -> Unit,
    enabledNeutral: Boolean = true,
    textNegative: String = stringResource(Res.string.cancel),
    styleNegative: UIAlertActionStyle = UIAlertActionStyle.Cancel,
    onNegative: () -> Unit,
    textPositive: String = stringResource(Res.string.ok),
    stylePositive: UIAlertActionStyle = UIAlertActionStyle.Default,
    onPositive: () -> Unit,
    enabledPositive: Boolean = true
)