package zone.ien.utils.ui.utils

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * keyboardAsState는 키보드의 표시 여부를 관찰하는 Composable 함수입니다.
 * 
 * 이 함수는 현재 키보드가 표시되고 있는지 여부를 State로 반환합니다.
 * 키보드가 표시되지 않을 경우, WindowInsets.ime.getBottom(LocalDensity.current)의 값이 150 이하로 판단됩니다.
 * 
 * @return 키보드 표시 여부를 나타내는 State<Boolean>
 */
@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 150
    return rememberUpdatedState(isImeVisible)
}

/**
 * advancedImePadding은 키보드에 따라 패딩을 조정하는 Modifier입니다.
 * 
 * 이 Modifier는 키보드가 표시되었을 때 자동으로 패딩을 조정하여 컴포저블 요소가 키보드에 가려지지 않도록 합니다.
 * 현재는 imePadding() 함수만 사용되지만, 주석 처리된 부분이 있어 필요 시 커스텀 로직을 추가할 수 있습니다.
 * 
 * @param condition 키보드 패딩 적용 여부
 * @return 키보드 패딩이 적용된 Modifier
 */
fun Modifier.advancedImePadding(condition: Boolean) = composed {
//    conditional(condition) {
//        var consumePadding by remember { mutableStateOf(0) }
//        onGloballyPositioned { coordinates ->
//            consumePadding = (coordinates.findRootCoordinates().size.height -
//                    (coordinates.positionInWindow().y + coordinates.size.height)).toInt().coerceAtLeast(0)
//        }
//            .consumeWindowInsets(
//                PaddingValues(bottom = with(LocalDensity.current) { consumePadding.toDp() })
//            )
//    }
//        .
    imePadding()
}

/**
 * dragToKeyboardClose는 키보드를 스와이프하여 닫는 기능을 제공하는 Modifier입니다.
 * 
 * 이 Modifier는 키보드가 표시된 상태에서 스크롤이 아래쪽으로 이동하면 키보드를 자동으로 닫습니다.
 * 
 * @param isKeyboardVisible 키보드 표시 여부
 * @return 키보드 닫기 기능이 포함된 Modifier
 */
@Composable
fun Modifier.dragToKeyboardClose(isKeyboardVisible: Boolean): Modifier {
    val keyboardManager = LocalSoftwareKeyboardController.current

    return pointerInput(isKeyboardVisible) {
        if (!isKeyboardVisible) return@pointerInput
        detectVerticalDragGestures { change, _ ->
            if (change.positionChange().y > 0) {
                keyboardManager?.hide()
            }
        }
    }
}