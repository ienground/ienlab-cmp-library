package zone.ien.utils.example

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIViewController
import zone.ien.hig.enableCupertinoParallelRenderingFix
import zone.ien.utils.ui.window.enableIenOverlayAppearanceFix

@OptIn(ExperimentalComposeUiApi::class)
fun MainViewController(): UIViewController {
    configureIosTextContextMenu()
    check(enableCupertinoParallelRenderingFix()) { "Compose 병렬 렌더링 보정을 적용하지 못했습니다" }
    check(enableIenOverlayAppearanceFix()) { "Compose 오버레이 수명주기 보정을 적용하지 못했습니다" }
    val isDeviceStressRun = NSProcessInfo.processInfo.arguments.contains("--ien-device-stress")
    return ComposeUIViewController(
//    configure = {
//        parallelRendering = false
//    }
    ) {
        if (isDeviceStressRun) DeviceStressScreen() else App()
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun configureIosTextContextMenu() {
    // 포커스 전 클립보드 조회가 없는 iOS 메뉴 경로를 사용합니다.
    ComposeFoundationFlags.isNewContextMenuEnabled = true
}
