package zone.ien.utils.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitViewController
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIViewController
import zone.ien.navigation.NativeNavigationViewController
import zone.ien.utils.initializeIenIos

internal actual val usesNativeNavigationPlatform: Boolean = true

/**
 * iOS 호환 보정을 초기화하고 UIKit 네이티브 내비게이션을 Compose 호스트에 연결합니다.
 * 앱에서는 [BaseNavigationFrame]으로 공통 테마·DI·화면을 구성합니다.
 *
 * @throws IllegalStateException 메인 스레드가 아니거나 iOS 호환 보정을 적용하지 못한 경우
 */
@OptIn(ExperimentalForeignApi::class)
fun BaseNavigationViewController(content: @Composable () -> Unit): UIViewController {
    initializeIenIos()
    return ComposeUIViewController {
        UIKitViewController(
            factory = { NativeNavigationViewController(content = content) },
            modifier = Modifier.fillMaxSize(),
            properties = UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.NonCooperative,
                isNativeAccessibilityEnabled = true,
            ),
        )
    }
}
