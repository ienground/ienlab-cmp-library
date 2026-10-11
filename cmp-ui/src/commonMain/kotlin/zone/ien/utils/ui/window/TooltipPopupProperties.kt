package zone.ien.utils.ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.PopupProperties

internal expect fun ienTooltipPopupProperties(): PopupProperties

/** iOS 복귀 시 초기화된 입력 범위를 복구하도록 팝업의 재생성 키를 제공합니다. */
@Composable
internal expect fun rememberIenTooltipPopupKey(): Int
