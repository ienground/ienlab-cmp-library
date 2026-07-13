package zone.ien.utils.ui.dialog

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import zone.ien.utils.cmp_ui.generated.resources.Res
import zone.ien.utils.cmp_ui.generated.resources.move_to_app_store

/**
 * iOS 플랫폼에서 업데이트 알림 다이얼로그의 이동/취소 버튼에 표시할 텍스트입니다.
 *
 * App Store 이동을 나타내는 텍스트 리소스를 반환합니다.
 */
internal actual val updateAlertDismissText @Composable get() = stringResource(Res.string.move_to_app_store)