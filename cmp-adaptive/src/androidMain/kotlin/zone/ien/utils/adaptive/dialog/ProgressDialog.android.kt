package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.dialog.IenProgressDialog

/**
 * 진행 상태 다이얼로그 컴포저블 (안드로이드 플랫폼 구현)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param isLoadingIndicator 로딩 인디케이터 표시 여부
 * @param isWavyIndicator 웨이브 인디케이터 표시 여부
 * @return 진행 상태 다이얼로그 컴포저블 (안드로이드)
 */
@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean,
    isWavyIndicator: Boolean
) {
    IenProgressDialog(
        modifier = modifier,
        visible = visible,
        isLoadingIndicator = isLoadingIndicator,
        isWavyIndicator = isWavyIndicator
    )
}

/**
 * 진행 상태 다이얼로그 컴포저블 (안드로이드 플랫폼 구현 - 진행률 기반)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param progress 진행률을 계산하는 함수
 * @param isWavyIndicator 웨이브 인디케이터 표시 여부
 * @return 진행 상태 다이얼로그 컴포저블 (안드로이드)
 */
@Composable
actual fun ProgressDialog(
    modifier: Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean
) {
    IenProgressDialog(
        modifier = modifier,
        visible = visible,
        progress = progress,
        isWavyIndicator = isWavyIndicator
    )
}