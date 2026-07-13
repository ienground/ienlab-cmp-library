package zone.ien.utils.ui.dialog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * IenProgressDialog은 진행 상태를 표시하는 다이얼로그를 제공하는 컴포저블입니다.
 *
 * @param modifier 다이얼로그에 적용할 Modifier
 * @param visible 다이얼로그의 표시 여부
 * @param isLoadingIndicator 로딩 인디케이터 표시 여부
 * @param isWavyIndicator 파도 모양 인디케이터 여부
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    isLoadingIndicator: Boolean = true,
    isWavyIndicator: Boolean = true
) {
    IenDialogFrame(
        visible = visible,
        onDismiss = {},
        modifier = modifier,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoadingIndicator) {
                LoadingIndicator(modifier = Modifier.padding(16.dp))
            } else if (isWavyIndicator) {
                CircularWavyProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IenProgressDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    progress: () -> Float,
    isWavyIndicator: Boolean = true
) {
    val currentProgress by animateFloatAsState(
        targetValue = progress.invoke().let { if (it > 1f) 1f else if (it < 0f) 0f else it }
    )

    IenDialogFrame(
        visible = visible,
        onDismiss = {},
        modifier = modifier,
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isWavyIndicator) {
                CircularWavyProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                CircularProgressIndicator(
                    progress = { currentProgress },
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
