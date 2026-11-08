package zone.ien.utils.adaptive.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import zone.ien.utils.ui.foundation.IenSemanticTone

/**
 * 진행 상태 다이얼로그 컴포저블
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param isLoadingIndicator 로딩 인디케이터 표시 여부
 * @param isWavyIndicator 웨이브 인디케이터 표시 여부
 * @param tone 강조 요소에 적용할 의미 색상 톤
 * @return 진행 상태 다이얼로그 컴포저블
 */
@Composable
expect fun ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean = true,
    isWavyIndicator: Boolean = true,
    tone: IenSemanticTone = IenSemanticTone.Brand,
)

/**
 * 진행 상태 다이얼로그 컴포저블 (진행률 기반)
 *
 * @param modifier 사용자 정의 스타일을 적용하기 위해 사용되는 Modifier
 * @param visible 다이얼로그 표시 여부
 * @param progress 진행률을 계산하는 함수
 * @param isWavyIndicator 웨이브 인디케이터 표시 여부
 * @param tone 강조 요소에 적용할 의미 색상 톤
 * @return 진행 상태 다이얼로그 컴포저블
 */
@Composable
expect fun ProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean = true,
    tone: IenSemanticTone = IenSemanticTone.Brand,
)