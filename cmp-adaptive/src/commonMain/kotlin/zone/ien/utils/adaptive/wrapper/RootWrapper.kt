package zone.ien.utils.adaptive.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.adaptive.AdaptiveScaffold
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.adaptive.ExperimentalAdaptiveApi
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.inputactions.InputAction
import zone.ien.inputactions.InputActionStyle
import zone.ien.inputactions.InputActionsHost
import zone.ien.inputactions.inputActions
import zone.ien.utils.isIos
import zone.ien.utils.ui.interactive.IenTextField
import zone.ien.utils.ui.screen.LocalEnableImePadding
import zone.ien.utils.ui.screen.LocalSetEnableImePadding
import zone.ien.utils.ui.utils.advancedImePadding
import zone.ien.utils.ui.utils.conditional
import zone.ien.utils.ui.utils.dragToKeyboardClose
import zone.ien.utils.ui.utils.keyboardAsState

/**
 * 루트 래퍼 컴포저블
 * 
 * 앱의 루트 레이아웃에 대한 적응형 래퍼 컴포저블입니다.
 * 키보드 처리 및 레이아웃 조정을 제공합니다.
 * 
 * @param modifier 래퍼에 적용할 수정자
 * @param enableImePadding 키보드 입력 패딩 사용 여부
 * @param notification 알림 콘텐츠 컴포저블
 * @param content 메인 콘텐츠 컴포저블
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun RootWrapper(
    modifier: Modifier = Modifier,
    enableImePadding: Boolean = true,
    notification: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    val backdrop = rememberDefaultBackdrop()
    var localEnableImePadding by remember { mutableStateOf(enableImePadding) }

    InputActionsHost {
        CompositionLocalProvider(
            LocalEnableImePadding provides localEnableImePadding,
            LocalSetEnableImePadding provides { localEnableImePadding = it }
        ) {
            AdaptiveScaffold(
                contentWindowInsets = WindowInsets(0.dp),
                modifier = modifier
            ) {
                Box(
                    modifier = Modifier
                        .conditional(localEnableImePadding) { advancedImePadding(!isIos) }
                        .dragToKeyboardClose(isKeyboardVisible)
                        .padding(it)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        content(
                            Modifier
                                .layerBackdrop(backdrop)
                        )
                    }

                    AdaptiveWidget(
                        material = {},
                        cupertino = {
                            notification()
                        }
                    )
                }
            }
        }
    }
}
