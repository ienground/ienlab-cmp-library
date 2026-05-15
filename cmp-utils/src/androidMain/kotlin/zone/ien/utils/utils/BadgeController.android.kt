package zone.ien.utils.utils

/**
 * Android는 Notification 발생 시 배지 생성되므로 별도 코드 없음
 */
actual class BadgeController actual constructor() {
    actual fun updateBadge(badges: Int) {}
}