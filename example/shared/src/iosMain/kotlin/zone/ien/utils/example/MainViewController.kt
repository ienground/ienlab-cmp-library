package zone.ien.utils.example

import androidx.compose.ui.window.ComposeUIViewController
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIViewController
import zone.ien.utils.initializeIenIos

fun MainViewController(): UIViewController {
    initializeIenIos()
    val isDeviceStressRun = NSProcessInfo.processInfo.arguments.contains("--ien-device-stress")
    return ComposeUIViewController {
        if (isDeviceStressRun) DeviceStressScreen() else App()
    }
}
