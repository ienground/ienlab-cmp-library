package zone.ien.utils.utils

/**
 * 애플리케이션을 백그라운드로 이동합니다.
 *
 * 이 함수는 애플리케이션을 백그라운드로 이동하는 교차 플랫폼 방식을 제공합니다.
 * 안드로이드에서는 ActivityManager를 사용하여 앱 태스크를 종료하고 제거하고,
 * iOS에서는 UIApplication에 "suspend" 작업을 전송합니다.
 */
expect fun moveToBackground()