package zone.ien.utils.adaptive.wrapper

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kyant.backdrop.backdrops.layerBackdrop
import zone.ien.hig.LocalCupertinoRightSystemArea
import zone.ien.hig.adaptive.AdaptiveWidget
import zone.ien.hig.utils.rememberDefaultBackdrop
import zone.ien.inputactions.InputActionsHost
import zone.ien.utils.adaptive.screen.AdaptiveScaffold
import zone.ien.utils.adaptive.screen.LocalAdaptiveSideBarLayout
import zone.ien.utils.ui.screen.isIenSideBarAvailable
import zone.ien.utils.ui.screen.LocalIenSideBarEnabled
import zone.ien.utils.ui.feedback.IenToastProvider
import zone.ien.utils.ui.foundation.IenTheme
import zone.ien.utils.ui.utils.dragToKeyboardClose
import zone.ien.utils.ui.utils.keyboardAsState

/**
 * 루트 래퍼 컴포저블
 * 
 * 앱의 루트 레이아웃에 대한 적응형 래퍼 컴포저블입니다.
 * 키보드 처리 및 레이아웃 조정을 제공합니다. 시스템 예약 영역은 내부 화면의 스캐폴드가 처리합니다.
 * 
 * @param modifier 래퍼에 적용할 수정자
 * @param notification 알림 콘텐츠 컴포저블
 * @param content 메인 콘텐츠 컴포저블
 */
@Composable
fun RootWrapper(
    modifier: Modifier = Modifier,
    notification: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val isKeyboardVisible by keyboardAsState()
    val backdrop = rememberDefaultBackdrop()
    val ownsRightSystemArea = LocalCupertinoRightSystemArea.current
    val sideBarEnabled = LocalIenSideBarEnabled.current
    val isSideBar = ownsRightSystemArea && isIenSideBarAvailable

    IenToastProvider(modifier = modifier) {
        InputActionsHost {
            CompositionLocalProvider(LocalCupertinoRightSystemArea provides false) {
                AdaptiveScaffold(
                    contentWindowInsets = WindowInsets(IenTheme.spacing.none),
                ) {
                    CompositionLocalProvider(
                        LocalCupertinoRightSystemArea provides ownsRightSystemArea,
                        LocalIenSideBarEnabled provides sideBarEnabled,
                        LocalAdaptiveSideBarLayout provides isSideBar,
                    ) {
                        Box(
                            modifier = Modifier
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
    }
}
