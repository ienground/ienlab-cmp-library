package zone.ien.utils.utils

/**
 * Android는 Notification 발생 시 배지 생성되므로 별도 코드 없음
 */
actual object BadgeController {
    actual fun updateBadge(badges: Int) {
        // Android implementation is intentionally left empty as badge management is handled via notifications
    }
}