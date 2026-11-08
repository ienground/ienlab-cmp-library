package zone.ien.utils

import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSThread
import zone.ien.hig.enableCupertinoParallelRenderingFix
import zone.ien.utils.ui.appearance.IENEnableIenOverlayAppearanceFix

/**
 * iOS 앱에서 사용하는 Compose 텍스트 메뉴와 렌더링·오버레이 호환 보정을 초기화합니다.
 *
 * 메인 스레드에서 첫 `ComposeUIViewController`를 만들기 전에 호출해야 합니다.
 * 포커스 전 클립보드 조회가 없는 새 텍스트 메뉴를 활성화하고, 병렬 렌더링 버퍼와
 * 오버레이의 UIKit appearance 전환을 보정합니다. 반복 호출해도 보정을 중복 설치하지 않습니다.
 * Compose 내부 구현에 의존하므로 Compose 버전을 변경하면 호환성을 다시 확인해야 합니다.
 *
 * @throws IllegalStateException 메인 스레드가 아니거나 호환 보정을 적용하지 못한 경우
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalForeignApi::class)
fun initializeIenIos() {
    check(NSThread.isMainThread) { "iOS 초기화는 메인 스레드에서 호출해야 합니다" }
    ComposeFoundationFlags.isNewContextMenuEnabled = true
    check(enableCupertinoParallelRenderingFix()) { "Compose 병렬 렌더링 보정을 적용하지 못했습니다" }
    check(IENEnableIenOverlayAppearanceFix()) { "Compose 오버레이 수명주기 보정을 적용하지 못했습니다" }
}
