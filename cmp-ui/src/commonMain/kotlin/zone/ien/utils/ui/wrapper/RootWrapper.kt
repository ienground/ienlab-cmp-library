package zone.ien.utils.ui.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import zone.ien.utils.ui.screen.IenScaffold
import zone.ien.utils.ui.utils.dragToKeyboardClose
import zone.ien.utils.ui.utils.keyboardAsState

/**
 * Material3 루트 래퍼 컴포저블
 * 
 * 이 컴포저블은 앱의 기본 루트 래퍼로 사용되며, 
 * 키보드와 관련된 동작을 처리하고 화면의 레이아웃을 관리합니다.
 * 
 * @param modifier 적용할 Modifier
 * @param content 내부 콘텐츠를 지정하는 컴포저블 블록
 */
@Composable
fun IenRootWrapper(
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()

    IenScaffold(
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .dragToKeyboardClose(isKeyboardVisible)
                .padding(it)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content(Modifier.weight(1f))
            }
        }
    }
}
