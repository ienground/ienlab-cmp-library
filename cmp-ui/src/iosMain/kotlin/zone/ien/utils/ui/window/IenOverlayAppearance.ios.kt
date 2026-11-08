package zone.ien.utils.ui.window

import kotlinx.cinterop.ExperimentalForeignApi
import zone.ien.utils.ui.appearance.IENEnableIenOverlayAppearanceFix

/**
 * iOS Compose 오버레이의 UIKit appearance 전환 호환 보정을 명시적으로 설치합니다.
 *
 * 첫 Compose 뷰 컨트롤러를 만들기 전에 메인 스레드에서 호출해야 합니다.
 * UIKit appearance 콜백을 프로세스 전역에서 관찰하지만, 중복 전환 억제는
 * 내부 `ComposeLayersViewController` 클래스에만 적용합니다. 이 내부 클래스의
 * 이름과 상속 구조에 의존하며, 해당 클래스가 `begin/endAppearanceTransition`을
 * 직접 재정의한 경우에는 원래 동작을 보존하고 보정을 적용하지 않습니다.
 *
 * @return 관찰기 설치에 성공했거나 이미 설치되어 있으면 true. 메인 스레드가 아니거나 UIKit 메서드 시그니처가 호환되지 않으면 false
 */
@OptIn(ExperimentalForeignApi::class)
fun enableIenOverlayAppearanceFix(): Boolean = IENEnableIenOverlayAppearanceFix()
