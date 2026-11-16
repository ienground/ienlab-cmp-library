package zone.ien.utils.example

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.key
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSProcessInfo
import platform.UIKit.UIViewController
import zone.ien.navigation.NativeNavigationViewController
import zone.ien.utils.initializeIenIos

@OptIn(ExperimentalForeignApi::class)
fun MainViewController(): UIViewController {
    initializeIenIos()
    val isDeviceStressRun = NSProcessInfo.processInfo.arguments.contains("--ien-device-stress")
    val state = SampleAppState()
    var nativeNavigation by mutableStateOf(false)
    val onNativeNavigationChanged: (Boolean) -> Unit = { nativeNavigation = it }
    val composeController by lazy {
        ComposeUIViewController { App(state, onNativeNavigationChanged = onNativeNavigationChanged) }
    }
    val nativeController by lazy {
        NativeNavigationViewController {
            App(state, nativeNavigation = true, onNativeNavigationChanged = onNativeNavigationChanged)
        }
    }
    return ComposeUIViewController {
        if (isDeviceStressRun) {
            DeviceStressScreen()
        } else {
            key(nativeNavigation) {
                UIKitViewController(
                    factory = { if (nativeNavigation) nativeController else composeController },
                    modifier = Modifier.fillMaxSize(),
                    properties = UIKitInteropProperties(
                        interactionMode = UIKitInteropInteractionMode.NonCooperative,
                        isNativeAccessibilityEnabled = true,
                    ),
                )
            }
        }
    }
}
