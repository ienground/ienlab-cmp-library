package zone.ien.utils.example

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import zone.ien.hig.enableCupertinoParallelRenderingFix

@OptIn(ExperimentalComposeUiApi::class)
fun MainViewController(): UIViewController {
    check(enableCupertinoParallelRenderingFix()) { "Compose 병렬 렌더링 보정을 적용하지 못했습니다" }
    return ComposeUIViewController(
//    configure = {
//        parallelRendering = false
//    }
    ) { App() }
}
